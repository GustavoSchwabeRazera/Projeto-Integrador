package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.AvisoPrazo;
import model.RegistroHistorico;
import model.Solicitacao;

// Pedidos ficam na tabela Solicitacoes e os empréstimos aceitos na tabela Emprestimo.
// O status do livro (disponível/indisponível) é trocado pelos triggers da tabela Emprestimo.
public class EmprestimoDAO {

    // Prazo padrão de um empréstimo (em dias)
    private static final int DIAS_EMPRESTIMO = 14;

    // Depois desse tempo sem resposta o pedido vence (em dias)
    private static final int DIAS_VALIDADE_PEDIDO = 14;

    // Pedido ainda não venceu
    private static final String PEDIDO_VALIDO =
            "s.data_pedido >= DATE_SUB(CURDATE(), INTERVAL " + DIAS_VALIDADE_PEDIDO + " DAY)";

    private final HistoricoDAO historicoDAO = new HistoricoDAO();

    // Sempre devolve uma conexão aberta
    private Connection getConn() throws SQLException {
        return ConnectionFactory.getConnection();
    }

    // =========================================================
    // SOLICITAR EMPRÉSTIMO
    // Fica na tabela Solicitacoes até o dono aceitar
    // =========================================================

    public void solicitar(String cpfUsuario, String isbn) throws SQLException {

        String sql = "INSERT INTO Solicitacoes (CPF_solicitante, ISBN, data_pedido) "
                + "VALUES (?, ?, CURDATE())";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, isbn);

            stmt.executeUpdate();
        }
    }

    // =========================================================
    // VERIFICA SE O USUÁRIO JÁ PEDIU ESSE LIVRO
    // =========================================================

    public boolean jaSolicitou(String cpfUsuario, String isbn) throws SQLException {

        String sql = "SELECT 1 FROM Solicitacoes s "
                + "WHERE s.CPF_solicitante = ? AND s.ISBN = ? AND " + PEDIDO_VALIDO;

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // =========================================================
    // VERIFICA SE O LIVRO ESTÁ DISPONÍVEL (NÃO EMPRESTADO)
    // =========================================================

    public boolean livroDisponivel(String isbn) throws SQLException {

        String sql = "SELECT status FROM Livros WHERE ISBN = ?";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean("status");
            }
        }
    }

    // =========================================================
    // LISTAR SOLICITAÇÕES PENDENTES DOS LIVROS DE UM USUÁRIO
    // =========================================================

    public List<Solicitacao> listarPendentesDoDono(String cpfDono) throws SQLException {

        List<Solicitacao> solicitacoes = new ArrayList<>();

        String sql = "SELECT s.id_solicitacao, s.data_pedido, l.ISBN, l.titulo, "
                + "u.nome, u.email, u.telefone, u.foto "
                + "FROM Solicitacoes s "
                + "JOIN Livros l ON l.ISBN = s.ISBN "
                + "JOIN Usuarios u ON u.CPF = s.CPF_solicitante "
                + "WHERE l.CPF_dono = ? AND " + PEDIDO_VALIDO + " "
                + "ORDER BY s.data_pedido, s.id_solicitacao";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfDono);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Solicitacao s = new Solicitacao();
                    s.setIdSolicitacao(rs.getInt("id_solicitacao"));
                    s.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                    s.setIsbn(rs.getString("ISBN"));
                    s.setTituloLivro(rs.getString("titulo"));
                    s.setNomeSolicitante(rs.getString("nome"));
                    s.setEmailSolicitante(rs.getString("email"));
                    s.setTelefoneSolicitante(rs.getString("telefone"));
                    s.setFotoSolicitante(rs.getBytes("foto"));
                    solicitacoes.add(s);
                }
            }
        }

        return solicitacoes;
    }

    // =========================================================
    // EMPRÉSTIMOS ATIVOS DO USUÁRIO QUE ACABAM NOS PRÓXIMOS DIAS
    // =========================================================

    public List<AvisoPrazo> listarAtivosPertoDoFim(String cpfUsuario, int dias) throws SQLException {

        List<AvisoPrazo> avisos = new ArrayList<>();

        String sql = "SELECT e.id_emprestimo, e.data_entrega, e.ISBN, e.titulo_livro, u.nome "
                + "FROM Emprestimo e "
                + "JOIN Usuarios u ON u.CPF = e.CPF_dono "
                + "WHERE e.CPF_leitor = ? AND e.data_devolucao IS NULL "
                + "AND e.data_entrega BETWEEN ? AND ? "
                + "ORDER BY e.data_entrega, e.id_emprestimo";

        LocalDate hoje = LocalDate.now();

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setDate(2, java.sql.Date.valueOf(hoje));
            stmt.setDate(3, java.sql.Date.valueOf(hoje.plusDays(dias)));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AvisoPrazo a = new AvisoPrazo();
                    a.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    a.setDataTermino(rs.getDate("data_entrega").toLocalDate());
                    a.setIsbn(rs.getString("ISBN"));
                    a.setTituloLivro(rs.getString("titulo_livro"));
                    a.setNomeDono(rs.getString("nome"));
                    avisos.add(a);
                }
            }
        }

        return avisos;
    }

    // =========================================================
    // EVENTOS DO CALENDÁRIO
    // Início e entrega dos empréstimos do usuário, tanto dos
    // livros que ele pegou quanto dos que emprestou. Pedidos
    // ainda não aceitos aparecem no dia em que foram feitos.
    // =========================================================

    public Map<LocalDate, List<String>> listarEventosCalendario(String cpfUsuario) throws SQLException {

        Map<LocalDate, List<String>> eventos = new HashMap<>();

        // Pedidos pendentes (feitos ou recebidos pelo usuário)
        String sqlPedidos = "SELECT s.data_pedido, s.CPF_solicitante, l.titulo, "
                + "dono.nome AS nome_dono, leitor.nome AS nome_leitor "
                + "FROM Solicitacoes s "
                + "JOIN Livros l ON l.ISBN = s.ISBN "
                + "JOIN Usuarios dono ON dono.CPF = l.CPF_dono "
                + "JOIN Usuarios leitor ON leitor.CPF = s.CPF_solicitante "
                + "WHERE " + PEDIDO_VALIDO + " "
                + "AND (s.CPF_solicitante = ? OR l.CPF_dono = ?) "
                + "ORDER BY s.data_pedido, s.id_solicitacao";

        try (PreparedStatement stmt = getConn().prepareStatement(sqlPedidos)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate data = rs.getDate("data_pedido").toLocalDate();
                    String titulo = rs.getString("titulo");

                    if (cpfUsuario.equals(rs.getString("CPF_solicitante"))) {
                        adicionarEvento(eventos, data, "Pediu \"" + titulo + "\" para "
                                + rs.getString("nome_dono") + " (aguardando)");
                    } else {
                        adicionarEvento(eventos, data, rs.getString("nome_leitor") + " pediu \""
                                + titulo + "\" (aguardando você)");
                    }
                }
            }
        }

        // Empréstimos aceitos
        String sqlEmprestimos = "SELECT e.data_emprestimo, e.data_entrega, e.data_devolucao, "
                + "e.CPF_leitor, e.titulo_livro, dono.nome AS nome_dono, leitor.nome AS nome_leitor "
                + "FROM Emprestimo e "
                + "JOIN Usuarios dono ON dono.CPF = e.CPF_dono "
                + "JOIN Usuarios leitor ON leitor.CPF = e.CPF_leitor "
                + "WHERE e.CPF_leitor = ? OR e.CPF_dono = ? "
                + "ORDER BY e.data_emprestimo, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sqlEmprestimos)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate inicio = rs.getDate("data_emprestimo").toLocalDate();
                    LocalDate entrega = rs.getDate("data_entrega").toLocalDate();
                    java.sql.Date devolucao = rs.getDate("data_devolucao");
                    String titulo = rs.getString("titulo_livro");

                    if (cpfUsuario.equals(rs.getString("CPF_leitor"))) {
                        // Livro que o usuário pegou emprestado
                        String dono = rs.getString("nome_dono");
                        adicionarEvento(eventos, inicio, "Pegou \"" + titulo + "\" de " + dono);

                        if (devolucao == null) {
                            adicionarEvento(eventos, entrega, "Devolver \"" + titulo + "\" para " + dono);
                        } else {
                            adicionarEvento(eventos, devolucao.toLocalDate(),
                                    "Devolveu \"" + titulo + "\" para " + dono);
                        }
                    } else {
                        // Livro do usuário emprestado para outra pessoa
                        String leitor = rs.getString("nome_leitor");
                        adicionarEvento(eventos, inicio, "Emprestou \"" + titulo + "\" para " + leitor);

                        if (devolucao == null) {
                            adicionarEvento(eventos, entrega, "Receber \"" + titulo + "\" de " + leitor);
                        } else {
                            adicionarEvento(eventos, devolucao.toLocalDate(),
                                    "Recebeu \"" + titulo + "\" de " + leitor);
                        }
                    }
                }
            }
        }

        return eventos;
    }

    private void adicionarEvento(Map<LocalDate, List<String>> eventos, LocalDate data, String texto) {
        eventos.computeIfAbsent(data, d -> new ArrayList<>()).add(texto);
    }

    // =========================================================
    // ACEITAR SOLICITAÇÃO
    // Cria o empréstimo (o trigger deixa o livro indisponível)
    // e apaga os pedidos desse livro, inclusive o aceito
    // =========================================================

    public void aceitar(Solicitacao solicitacao) throws SQLException {

        LocalDate hoje = LocalDate.now();

        String cpfDono = null;
        String nomeDono = null;
        String cpfLeitor = null;

        Connection connection = getConn();
        boolean autoCommitAnterior = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            // Dono, leitor e título vêm do pedido e do livro
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Emprestimo "
                    + "(ISBN, titulo_livro, CPF_dono, CPF_leitor, data_emprestimo, data_entrega) "
                    + "SELECT l.ISBN, l.titulo, l.CPF_dono, s.CPF_solicitante, ?, ? "
                    + "FROM Solicitacoes s JOIN Livros l ON l.ISBN = s.ISBN "
                    + "WHERE s.id_solicitacao = ?")) {
                stmt.setDate(1, java.sql.Date.valueOf(hoje));
                stmt.setDate(2, java.sql.Date.valueOf(hoje.plusDays(DIAS_EMPRESTIMO)));
                stmt.setInt(3, solicitacao.getIdSolicitacao());
                stmt.executeUpdate();
            }

            // Para o histórico dos dois lados
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT l.CPF_dono, d.nome AS nome_dono, s.CPF_solicitante "
                    + "FROM Solicitacoes s "
                    + "JOIN Livros l ON l.ISBN = s.ISBN "
                    + "JOIN Usuarios d ON d.CPF = l.CPF_dono "
                    + "WHERE s.id_solicitacao = ?")) {

                stmt.setInt(1, solicitacao.getIdSolicitacao());

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        cpfDono = rs.getString("CPF_dono");
                        nomeDono = rs.getString("nome_dono");
                        cpfLeitor = rs.getString("CPF_solicitante");
                    }
                }
            }

            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM Solicitacoes WHERE ISBN = ?")) {
                stmt.setString(1, solicitacao.getIsbn());
                stmt.executeUpdate();
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommitAnterior);
        }

        // Histórico dos dois lados: o dono emprestou e o solicitante pegou emprestado
        if (cpfDono != null) {
            historicoDAO.registrarSemFalhar(
                    cpfDono,
                    RegistroHistorico.EMPRESTIMO_CONCEDIDO,
                    solicitacao.getIsbn(),
                    solicitacao.getTituloLivro(),
                    solicitacao.getNomeSolicitante());

            historicoDAO.registrarSemFalhar(
                    cpfLeitor,
                    RegistroHistorico.EMPRESTIMO_RECEBIDO,
                    solicitacao.getIsbn(),
                    solicitacao.getTituloLivro(),
                    nomeDono);
        }
    }

    // =========================================================
    // RECUSAR SOLICITAÇÃO (APAGA O PEDIDO)
    // =========================================================

    public void recusar(Solicitacao solicitacao) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "DELETE FROM Solicitacoes WHERE id_solicitacao = ?")) {
            stmt.setInt(1, solicitacao.getIdSolicitacao());
            stmt.executeUpdate();
        }
    }

    // =========================================================
    // QUEM ESTÁ COM O LIVRO AGORA
    // Ex.: "Lucas (entrega até 29/09/2026)", ou null se o livro
    // não está emprestado
    // =========================================================

    public String descreverEmprestimoAtivo(String isbn) throws SQLException {

        String sql = "SELECT u.nome, e.data_entrega "
                + "FROM Emprestimo e "
                + "JOIN Usuarios u ON u.CPF = e.CPF_leitor "
                + "WHERE e.ISBN = ? AND e.data_devolucao IS NULL";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate entrega = rs.getDate("data_entrega").toLocalDate();
                    return rs.getString("nome") + " (entrega até "
                            + entrega.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + ")";
                }
            }
        }

        return null;
    }

    // =========================================================
    // REGISTRAR DEVOLUÇÃO
    // Preenche a data de devolução; o trigger deixa o livro
    // disponível de novo
    // =========================================================

    public boolean registrarDevolucao(String isbn) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Emprestimo SET data_devolucao = CURDATE() "
                + "WHERE ISBN = ? AND data_devolucao IS NULL")) {
            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;
        }
    }
}
