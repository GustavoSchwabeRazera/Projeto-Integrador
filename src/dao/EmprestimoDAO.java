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

public class EmprestimoDAO {

    // Prazo padrão de um empréstimo (em dias)
    private static final int DIAS_EMPRESTIMO = 14;

    private final HistoricoDAO historicoDAO = new HistoricoDAO();

    // Sempre devolve uma conexão aberta
    private Connection getConn() throws SQLException {
        return ConnectionFactory.getConnection();
    }

    // =========================================================
    // SOLICITAR EMPRÉSTIMO
    // Fica com statusEmprestimo = FALSE até o dono aceitar
    // =========================================================

    public void solicitar(String cpfUsuario, String isbn) throws SQLException {

        String sql = "INSERT INTO Emprestimos "
                + "(id_emprestimo, CPF_usuario, ISBN, statusEmprestimo, data_inicio, data_termino) "
                + "VALUES (?, ?, ?, FALSE, ?, ?)";

        LocalDate hoje = LocalDate.now();

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setInt(1, proximoId());
            stmt.setString(2, cpfUsuario);
            stmt.setString(3, isbn);
            stmt.setDate(4, java.sql.Date.valueOf(hoje));
            stmt.setDate(5, java.sql.Date.valueOf(hoje.plusDays(DIAS_EMPRESTIMO)));

            stmt.executeUpdate();
        }
    }

    // =========================================================
    // VERIFICA SE O USUÁRIO JÁ PEDIU ESSE LIVRO
    // =========================================================

    public boolean jaSolicitou(String cpfUsuario, String isbn) throws SQLException {

        String sql = "SELECT 1 FROM Emprestimos "
                + "WHERE CPF_usuario = ? AND ISBN = ? AND statusEmprestimo = FALSE "
                + "AND data_termino >= CURDATE()";

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

        String sql = "SELECT e.id_emprestimo, e.data_inicio, l.ISBN, l.titulo, "
                + "u.nome, u.email, u.telefone, u.foto "
                + "FROM Emprestimos e "
                + "JOIN Livros l ON l.ISBN = e.ISBN "
                + "JOIN Usuarios u ON u.CPF = e.CPF_usuario "
                + "WHERE l.CPF_dono = ? AND e.statusEmprestimo = FALSE "
                + "AND e.data_termino >= CURDATE() "
                + "ORDER BY e.data_inicio, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfDono);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Solicitacao s = new Solicitacao();
                    s.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    s.setDataPedido(rs.getDate("data_inicio").toLocalDate());
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

        String sql = "SELECT e.id_emprestimo, e.data_termino, l.ISBN, l.titulo, u.nome "
                + "FROM Emprestimos e "
                + "JOIN Livros l ON l.ISBN = e.ISBN "
                + "JOIN Usuarios u ON u.CPF = l.CPF_dono "
                + "WHERE e.CPF_usuario = ? AND e.statusEmprestimo = TRUE "
                + "AND e.data_termino BETWEEN ? AND ? "
                + "ORDER BY e.data_termino, e.id_emprestimo";

        LocalDate hoje = LocalDate.now();

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setDate(2, java.sql.Date.valueOf(hoje));
            stmt.setDate(3, java.sql.Date.valueOf(hoje.plusDays(dias)));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AvisoPrazo a = new AvisoPrazo();
                    a.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    a.setDataTermino(rs.getDate("data_termino").toLocalDate());
                    a.setIsbn(rs.getString("ISBN"));
                    a.setTituloLivro(rs.getString("titulo"));
                    a.setNomeDono(rs.getString("nome"));
                    avisos.add(a);
                }
            }
        }

        return avisos;
    }

    // =========================================================
    // EVENTOS DO CALENDÁRIO
    // Início e devolução dos empréstimos do usuário, tanto dos
    // livros que ele pegou quanto dos que emprestou. Pedidos
    // ainda não aceitos aparecem no dia em que foram feitos.
    // =========================================================

    public Map<LocalDate, List<String>> listarEventosCalendario(String cpfUsuario) throws SQLException {

        Map<LocalDate, List<String>> eventos = new HashMap<>();

        String sql = "SELECT e.statusEmprestimo, e.data_inicio, e.data_termino, e.CPF_usuario, l.titulo, "
                + "dono.nome AS nome_dono, leitor.nome AS nome_leitor "
                + "FROM Emprestimos e "
                + "JOIN Livros l ON l.ISBN = e.ISBN "
                + "JOIN Usuarios dono ON dono.CPF = l.CPF_dono "
                + "JOIN Usuarios leitor ON leitor.CPF = e.CPF_usuario "
                // aceitos sempre; pedidos pendentes só enquanto não vencem
                + "WHERE (e.statusEmprestimo = TRUE OR e.data_termino >= CURDATE()) "
                + "AND (e.CPF_usuario = ? OR l.CPF_dono = ?) "
                + "ORDER BY e.data_inicio, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate inicio = rs.getDate("data_inicio").toLocalDate();
                    LocalDate termino = rs.getDate("data_termino").toLocalDate();
                    String titulo = rs.getString("titulo");
                    boolean pegouLivro = cpfUsuario.equals(rs.getString("CPF_usuario"));

                    if (!rs.getBoolean("statusEmprestimo")) {
                        // Pedido feito, aguardando o dono aceitar
                        if (pegouLivro) {
                            adicionarEvento(eventos, inicio, "Pediu \"" + titulo + "\" para "
                                    + rs.getString("nome_dono") + " (aguardando)");
                        } else {
                            adicionarEvento(eventos, inicio, rs.getString("nome_leitor") + " pediu \""
                                    + titulo + "\" (aguardando você)");
                        }
                    } else if (pegouLivro) {
                        // Livro que o usuário pegou emprestado
                        String dono = rs.getString("nome_dono");
                        adicionarEvento(eventos, inicio, "Pegou \"" + titulo + "\" de " + dono);
                        adicionarEvento(eventos, termino, "Devolver \"" + titulo + "\" para " + dono);
                    } else {
                        // Livro do usuário emprestado para outra pessoa
                        String leitor = rs.getString("nome_leitor");
                        adicionarEvento(eventos, inicio, "Emprestou \"" + titulo + "\" para " + leitor);
                        adicionarEvento(eventos, termino, "Receber \"" + titulo + "\" de " + leitor);
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
    // O empréstimo começa hoje, o livro fica indisponível e
    // os outros pedidos pendentes do mesmo livro são recusados
    // =========================================================

    public void aceitar(Solicitacao solicitacao) throws SQLException {

        LocalDate hoje = LocalDate.now();

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Emprestimos SET statusEmprestimo = TRUE, data_inicio = ?, data_termino = ? "
                + "WHERE id_emprestimo = ?")) {
            stmt.setDate(1, java.sql.Date.valueOf(hoje));
            stmt.setDate(2, java.sql.Date.valueOf(hoje.plusDays(DIAS_EMPRESTIMO)));
            stmt.setInt(3, solicitacao.getIdEmprestimo());
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Livros SET status = FALSE WHERE ISBN = ?")) {
            stmt.setString(1, solicitacao.getIsbn());
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = getConn().prepareStatement(
                "DELETE FROM Emprestimos WHERE ISBN = ? AND statusEmprestimo = FALSE "
                + "AND data_termino >= CURDATE() AND id_emprestimo <> ?")) {
            stmt.setString(1, solicitacao.getIsbn());
            stmt.setInt(2, solicitacao.getIdEmprestimo());
            stmt.executeUpdate();
        }

        // Histórico dos dois lados: o dono emprestou e o solicitante pegou emprestado
        try (PreparedStatement stmt = getConn().prepareStatement(
                "SELECT l.CPF_dono, d.nome AS nome_dono, e.CPF_usuario "
                + "FROM Emprestimos e "
                + "JOIN Livros l ON l.ISBN = e.ISBN "
                + "JOIN Usuarios d ON d.CPF = l.CPF_dono "
                + "WHERE e.id_emprestimo = ?")) {

            stmt.setInt(1, solicitacao.getIdEmprestimo());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    historicoDAO.registrarSemFalhar(
                            rs.getString("CPF_dono"),
                            RegistroHistorico.EMPRESTIMO_CONCEDIDO,
                            solicitacao.getIsbn(),
                            solicitacao.getTituloLivro(),
                            solicitacao.getNomeSolicitante());

                    historicoDAO.registrarSemFalhar(
                            rs.getString("CPF_usuario"),
                            RegistroHistorico.EMPRESTIMO_RECEBIDO,
                            solicitacao.getIsbn(),
                            solicitacao.getTituloLivro(),
                            rs.getString("nome_dono"));
                }
            }
        }
    }

    // =========================================================
    // RECUSAR SOLICITAÇÃO (APAGA O PEDIDO)
    // =========================================================

    public void recusar(Solicitacao solicitacao) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "DELETE FROM Emprestimos WHERE id_emprestimo = ? AND statusEmprestimo = FALSE")) {
            stmt.setInt(1, solicitacao.getIdEmprestimo());
            stmt.executeUpdate();
        }
    }

    // A tabela não tem AUTO_INCREMENT, então pega o maior id + 1
    private int proximoId() throws SQLException {

        String sql = "SELECT COALESCE(MAX(id_emprestimo), 0) + 1 FROM Emprestimos";

        try (PreparedStatement stmt = getConn().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            rs.next();
            return rs.getInt(1);
        }
    }
}
