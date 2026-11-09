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
import model.EmprestimoAtivo;
import model.RegistroHistorico;
import model.Solicitacao;


public class EmprestimoDAO {

    
    private static final int DIAS_EMPRESTIMO = 14;

  
    private static final int DIAS_VALIDADE_PEDIDO = Solicitacao.DIAS_VALIDADE;

   
    private static final String PEDIDO_VALIDO =
            "s.data_pedido >= CURRENT_DATE - INTERVAL '" + DIAS_VALIDADE_PEDIDO + "' DAY";

    private static boolean colunasVerificadas = false;

    private final HistoricoDAO historicoDAO = new HistoricoDAO();


    private Connection getConn() throws SQLException {

        Connection conn = ConnectionFactory.getConnection();

        // Cria a coluna se o banco MySQL foi montado antes dela existir
        if (!colunaVerificada && !ConnectionFactory.usaPostgres()) {

            criarColunaSeFaltar(conn, "devolucao_solicitada");
            criarColunaSeFaltar(conn, "ultimo_aviso_prazo");

            colunasVerificadas = true;
        }

        return conn;
    }

    private void criarColunaSeFaltar(Connection conn, String coluna) throws SQLException {

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT 1 FROM information_schema.COLUMNS "
                + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'Emprestimo' "
                + "AND COLUMN_NAME = ?")) {

            stmt.setString(1, coluna);

            try (ResultSet rs = stmt.executeQuery()) {

                if (!rs.next()) {
                    try (PreparedStatement alterar = conn.prepareStatement(
                            "ALTER TABLE Emprestimo ADD COLUMN " + coluna + " DATE NULL")) {
                        alterar.executeUpdate();
                    }
                }
            }
        }
    }


    public void solicitar(String cpfUsuario, String isbn) throws SQLException {

        String sql = "INSERT INTO Solicitacoes (CPF_solicitante, ISBN, data_pedido) "
                + "VALUES (?, ?, CURRENT_DATE)";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, isbn);

            stmt.executeUpdate();
        }
    }


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



    public boolean temEmprestimoAtivo(String cpfUsuario) throws SQLException {

        String sql = "SELECT 1 FROM Emprestimo "
                + "WHERE CPF_leitor = ? AND data_devolucao IS NULL LIMIT 1";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public boolean solicitanteTemEmprestimoAtivo(int idSolicitacao) throws SQLException {

        String sql = "SELECT 1 FROM Solicitacoes s "
                + "JOIN Emprestimo e ON e.CPF_leitor = s.CPF_solicitante "
                + "WHERE s.id_solicitacao = ? AND e.data_devolucao IS NULL LIMIT 1";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setInt(1, idSolicitacao);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }



    public boolean livroDisponivel(String isbn) throws SQLException {

        String sql = "SELECT status FROM Livros WHERE ISBN = ?";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() && rs.getBoolean("status");
            }
        }
    }



    public List<Solicitacao> listarPendentesDoDono(String cpfDono) throws SQLException {

        List<Solicitacao> solicitacoes = new ArrayList<>();

        String sql = "SELECT s.id_solicitacao, s.CPF_solicitante, s.data_pedido, l.ISBN, l.titulo, "
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
                    s.setCpfSolicitante(rs.getString("CPF_solicitante"));
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



    public List<Solicitacao> listarPedidosEnviados(String cpfSolicitante) throws SQLException {

        List<Solicitacao> pedidos = new ArrayList<>();

        String sql = "SELECT s.id_solicitacao, s.data_pedido, l.ISBN, l.titulo, u.nome, u.foto "
                + "FROM Solicitacoes s "
                + "JOIN Livros l ON l.ISBN = s.ISBN "
                + "JOIN Usuarios u ON u.CPF = l.CPF_dono "
                + "WHERE s.CPF_solicitante = ? AND " + PEDIDO_VALIDO + " "
                + "ORDER BY s.data_pedido, s.id_solicitacao";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfSolicitante);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Solicitacao s = new Solicitacao();
                    s.setIdSolicitacao(rs.getInt("id_solicitacao"));
                    s.setDataPedido(rs.getDate("data_pedido").toLocalDate());
                    s.setIsbn(rs.getString("ISBN"));
                    s.setTituloLivro(rs.getString("titulo"));
                    s.setNomeDono(rs.getString("nome"));
                    s.setFotoDono(rs.getBytes("foto"));
                    pedidos.add(s);
                }
            }
        }

        return pedidos;
    }


    public List<AvisoPrazo> listarPrazos(String cpfUsuario) throws SQLException {

        return buscarPrazos(cpfUsuario, "");
    }

    // Só os prazos que o usuário ainda não viu no aviso de hoje
    public List<AvisoPrazo> listarPrazosNaoAvisadosHoje(String cpfUsuario) throws SQLException {

        return buscarPrazos(cpfUsuario,
                "AND (e.ultimo_aviso_prazo IS NULL OR e.ultimo_aviso_prazo < CURDATE()) ");
    }

    private List<AvisoPrazo> buscarPrazos(String cpfUsuario, String filtro) throws SQLException {

        List<AvisoPrazo> avisos = new ArrayList<>();

        String sql = "SELECT e.id_emprestimo, e.data_entrega, "
                + "e.ISBN, e.titulo_livro, u.nome, u.foto "
                + "FROM Emprestimo e "
                + "JOIN Usuarios u ON u.CPF = e.CPF_dono "
                + "WHERE e.CPF_leitor = ? AND e.data_devolucao IS NULL "
                + "AND e.devolucao_solicitada IS NULL "
                + filtro
                + "ORDER BY e.data_entrega, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    AvisoPrazo a = new AvisoPrazo();
                    a.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    a.setDataTermino(rs.getDate("data_entrega").toLocalDate());
                    a.setIsbn(rs.getString("ISBN"));
                    a.setTituloLivro(rs.getString("titulo_livro"));
                    a.setNomeDono(rs.getString("nome"));
                    a.setFotoDono(rs.getBytes("foto"));
                    avisos.add(a);
                }
            }
        }

        return avisos;
    }

    
    public void marcarPrazosAvisadosHoje(String cpfUsuario) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Emprestimo SET ultimo_aviso_prazo = CURDATE() "
                + "WHERE CPF_leitor = ? AND data_devolucao IS NULL")) {
            stmt.setString(1, cpfUsuario);
            stmt.executeUpdate();
        }
    }


    public List<EmprestimoAtivo> listarEmprestimosAceitos(String cpfDono) throws SQLException {

        List<EmprestimoAtivo> emprestimos = new ArrayList<>();

        String sql = "SELECT e.id_emprestimo, e.ISBN, e.titulo_livro, "
                + "e.data_entrega, u.nome, u.foto "
                + "FROM Emprestimo e "
                + "JOIN Usuarios u ON u.CPF = e.CPF_leitor "
                + "WHERE e.CPF_dono = ? AND e.data_devolucao IS NULL "
                + "AND e.devolucao_solicitada IS NULL "
                + "ORDER BY e.data_entrega, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfDono);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmprestimoAtivo e = new EmprestimoAtivo();
                    e.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    e.setIsbn(rs.getString("ISBN"));
                    e.setTituloLivro(rs.getString("titulo_livro"));
                    e.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
                    e.setNomeLeitor(rs.getString("nome"));
                    e.setFotoLeitor(rs.getBytes("foto"));
                    emprestimos.add(e);
                }
            }
        }

        return emprestimos;
    }


    public Map<LocalDate, List<String>> listarEventosCalendario(String cpfUsuario) throws SQLException {

        Map<LocalDate, List<String>> eventos = new HashMap<>();

     
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

            String titulo;

            // Dono, leitor e título vêm do pedido e do livro
            try (PreparedStatement stmt = connection.prepareStatement(
                    "SELECT l.titulo, l.CPF_dono, d.nome AS nome_dono, s.CPF_solicitante "
                    + "FROM Solicitacoes s "
                    + "JOIN Livros l ON l.ISBN = s.ISBN "
                    + "JOIN Usuarios d ON d.CPF = l.CPF_dono "
                    + "WHERE s.id_solicitacao = ?")) {

                stmt.setInt(1, solicitacao.getIdSolicitacao());

                try (ResultSet rs = stmt.executeQuery()) {

                    if (!rs.next()) {
                        throw new SQLException("Essa solicitação não existe mais.");
                    }

                    titulo = rs.getString("titulo");
                    cpfDono = rs.getString("CPF_dono");
                    nomeDono = rs.getString("nome_dono");
                    cpfLeitor = rs.getString("CPF_solicitante");
                }
            }

            // Comando separado do SELECT acima: o trigger atualiza a tabela Livros,
            // e o MySQL não deixa isso se o próprio INSERT estiver lendo essa tabela
            try (PreparedStatement stmt = connection.prepareStatement(
                    "INSERT INTO Emprestimo "
                    + "(ISBN, titulo_livro, CPF_dono, CPF_leitor, data_emprestimo, data_entrega) "
                    + "VALUES (?, ?, ?, ?, ?, ?)")) {
                stmt.setString(1, solicitacao.getIsbn());
                stmt.setString(2, titulo);
                stmt.setString(3, cpfDono);
                stmt.setString(4, cpfLeitor);
                stmt.setDate(5, java.sql.Date.valueOf(hoje));
                stmt.setDate(6, java.sql.Date.valueOf(hoje.plusDays(DIAS_EMPRESTIMO)));
                stmt.executeUpdate();
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

        String sql = "SELECT u.nome, e.data_entrega, e.devolucao_solicitada "
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
                            + ")"
                            + (rs.getDate("devolucao_solicitada") != null
                                    ? " - devolução aguardando sua confirmação" : "");
                }
            }
        }

        return null;
    }

    // =========================================================
    // LIVROS QUE ESTÃO COM O USUÁRIO
    // =========================================================

    public List<EmprestimoAtivo> listarLivrosComigo(String cpfLeitor) throws SQLException {

        List<EmprestimoAtivo> emprestimos = new ArrayList<>();

        String sql = "SELECT e.id_emprestimo, e.ISBN, e.titulo_livro, e.data_entrega, "
                + "e.devolucao_solicitada, l.fotoCapa, u.nome, u.email, u.telefone "
                + "FROM Emprestimo e "
                + "JOIN Livros l ON l.ISBN = e.ISBN "
                + "JOIN Usuarios u ON u.CPF = e.CPF_dono "
                + "WHERE e.CPF_leitor = ? AND e.data_devolucao IS NULL "
                + "ORDER BY e.data_entrega, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfLeitor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmprestimoAtivo e = new EmprestimoAtivo();
                    e.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    e.setIsbn(rs.getString("ISBN"));
                    e.setTituloLivro(rs.getString("titulo_livro"));
                    e.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
                    java.sql.Date solicitada = rs.getDate("devolucao_solicitada");
                    if (solicitada != null) {
                        e.setDevolucaoSolicitada(solicitada.toLocalDate());
                    }
                    e.setFotoCapa(rs.getBytes("fotoCapa"));
                    e.setNomeDono(rs.getString("nome"));
                    e.setEmailDono(rs.getString("email"));
                    e.setTelefoneDono(rs.getString("telefone"));
                    emprestimos.add(e);
                }
            }
        }

        return emprestimos;
    }

    // =========================================================
    // O LEITOR AVISA QUE DEVOLVEU
    // O livro continua emprestado até o dono confirmar
    // =========================================================

    public void solicitarDevolucao(int idEmprestimo) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Emprestimo SET devolucao_solicitada = CURRENT_DATE "
                + "WHERE id_emprestimo = ? AND data_devolucao IS NULL")) {
            stmt.setInt(1, idEmprestimo);
            stmt.executeUpdate();
        }
    }

    // =========================================================
    // DEVOLUÇÕES ESPERANDO O DONO CONFIRMAR
    // =========================================================

    public List<EmprestimoAtivo> listarDevolucoesParaConfirmar(String cpfDono) throws SQLException {

        List<EmprestimoAtivo> devolucoes = new ArrayList<>();

        String sql = "SELECT e.id_emprestimo, e.ISBN, e.titulo_livro, e.data_entrega, "
                + "e.devolucao_solicitada, u.nome, u.foto "
                + "FROM Emprestimo e "
                + "JOIN Usuarios u ON u.CPF = e.CPF_leitor "
                + "WHERE e.CPF_dono = ? AND e.data_devolucao IS NULL "
                + "AND e.devolucao_solicitada IS NOT NULL "
                + "ORDER BY e.devolucao_solicitada, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfDono);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmprestimoAtivo e = new EmprestimoAtivo();
                    e.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    e.setIsbn(rs.getString("ISBN"));
                    e.setTituloLivro(rs.getString("titulo_livro"));
                    e.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
                    e.setDevolucaoSolicitada(rs.getDate("devolucao_solicitada").toLocalDate());
                    e.setNomeLeitor(rs.getString("nome"));
                    e.setFotoLeitor(rs.getBytes("foto"));
                    devolucoes.add(e);
                }
            }
        }

        return devolucoes;
    }

    // =========================================================
    // DEVOLUÇÕES QUE O USUÁRIO INFORMOU E O DONO AINDA NÃO CONFIRMOU
    // =========================================================

    public List<EmprestimoAtivo> listarDevolucoesInformadas(String cpfLeitor) throws SQLException {

        List<EmprestimoAtivo> devolucoes = new ArrayList<>();

        String sql = "SELECT e.id_emprestimo, e.ISBN, e.titulo_livro, e.data_entrega, "
                + "e.devolucao_solicitada, u.nome, u.foto "
                + "FROM Emprestimo e "
                + "JOIN Usuarios u ON u.CPF = e.CPF_dono "
                + "WHERE e.CPF_leitor = ? AND e.data_devolucao IS NULL "
                + "AND e.devolucao_solicitada IS NOT NULL "
                + "ORDER BY e.devolucao_solicitada, e.id_emprestimo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfLeitor);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    EmprestimoAtivo e = new EmprestimoAtivo();
                    e.setIdEmprestimo(rs.getInt("id_emprestimo"));
                    e.setIsbn(rs.getString("ISBN"));
                    e.setTituloLivro(rs.getString("titulo_livro"));
                    e.setDataEntrega(rs.getDate("data_entrega").toLocalDate());
                    e.setDevolucaoSolicitada(rs.getDate("devolucao_solicitada").toLocalDate());
                    e.setNomeDono(rs.getString("nome"));
                    e.setFotoDono(rs.getBytes("foto"));
                    devolucoes.add(e);
                }
            }
        }

        return devolucoes;
    }

    // =========================================================
    // O DONO NÃO RECEBEU O LIVRO
    // O leitor volta a ver o botão de devolver
    // =========================================================

    public void recusarDevolucao(int idEmprestimo) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Emprestimo SET devolucao_solicitada = NULL "
                + "WHERE id_emprestimo = ? AND data_devolucao IS NULL")) {
            stmt.setInt(1, idEmprestimo);
            stmt.executeUpdate();
        }
    }

    // =========================================================
    // REGISTRAR DEVOLUÇÃO
    // Preenche a data de devolução; o trigger deixa o livro
    // disponível de novo
    // =========================================================

    public boolean registrarDevolucao(String isbn) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Emprestimo SET data_devolucao = CURRENT_DATE "
                + "WHERE ISBN = ? AND data_devolucao IS NULL")) {
            stmt.setString(1, isbn);
            return stmt.executeUpdate() > 0;
        }
    }
}
