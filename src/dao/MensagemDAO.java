package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.ContatoChat;
import model.Mensagem;

// Duas pessoas podem conversar enquanto existir entre elas um pedido pendente
// ou o empréstimo que nasceu de um pedido aceito (o aceite apaga a linha de
// Solicitacoes, por isso os empréstimos ativos também contam).
public class MensagemDAO {

    // Mesmo prazo de validade dos pedidos usado em EmprestimoDAO (em dias)
    private static final int DIAS_VALIDADE_PEDIDO = 14;

    // Pares de pessoas que podem conversar
    private static final String VINCULOS =
            "(SELECT s.CPF_solicitante AS cpf_a, l.CPF_dono AS cpf_b "
            + "FROM Solicitacoes s JOIN Livros l ON l.ISBN = s.ISBN "
            + "WHERE s.data_pedido >= DATE_SUB(CURDATE(), INTERVAL " + DIAS_VALIDADE_PEDIDO + " DAY) "
            + "UNION "
            + "SELECT e.CPF_leitor, e.CPF_dono FROM Emprestimo e "
            + "WHERE e.data_devolucao IS NULL) v";

    // CPFs com quem o usuário pode conversar (recebe o CPF dele duas vezes)
    private static final String CONTATOS =
            "SELECT v.cpf_b FROM " + VINCULOS + " WHERE v.cpf_a = ? "
            + "UNION "
            + "SELECT v.cpf_a FROM " + VINCULOS + " WHERE v.cpf_b = ?";

    private static boolean tabelaVerificada = false;

    // Null = usa a conexão compartilhada do sistema
    private final Connection conexaoPropria;

    public MensagemDAO() {
        this(null);
    }

    public MensagemDAO(Connection conexaoPropria) {
        this.conexaoPropria = conexaoPropria;
    }

    // Sempre devolve uma conexão aberta
    private Connection getConn() throws SQLException {

        Connection conn = conexaoPropria != null
                ? conexaoPropria
                : ConnectionFactory.getConnection();

        // Cria a tabela se o banco foi montado antes dela existir
        if (!tabelaVerificada) {

            try (PreparedStatement stmt = conn.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS Mensagens ("
                    + "id_mensagem INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                    + "CPF_remetente VARCHAR(11) NOT NULL, "
                    + "CPF_destinatario VARCHAR(11) NOT NULL, "
                    + "texto VARCHAR(500) NOT NULL, "
                    + "data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                    + "lida BOOLEAN NOT NULL DEFAULT FALSE, "
                    + "FOREIGN KEY (CPF_remetente) REFERENCES Usuarios(CPF) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE, "
                    + "FOREIGN KEY (CPF_destinatario) REFERENCES Usuarios(CPF) "
                    + "ON UPDATE CASCADE ON DELETE CASCADE)")) {

                stmt.executeUpdate();
            }

            tabelaVerificada = true;
        }

        return conn;
    }

    // =========================================================
    // QUEM PODE CONVERSAR COM QUEM
    // =========================================================

    public boolean podeConversar(String cpfA, String cpfB) throws SQLException {

        if (cpfA.equals(cpfB)) {
            return false;
        }

        String sql = "SELECT 1 FROM " + VINCULOS + " "
                + "WHERE (v.cpf_a = ? AND v.cpf_b = ?) OR (v.cpf_a = ? AND v.cpf_b = ?) LIMIT 1";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfA);
            stmt.setString(2, cpfB);
            stmt.setString(3, cpfB);
            stmt.setString(4, cpfA);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // =========================================================
    // CONTATOS DO USUÁRIO, COM AS MENSAGENS NÃO LIDAS DE CADA UM
    // =========================================================

    public List<ContatoChat> listarContatos(String cpfUsuario) throws SQLException {

        List<ContatoChat> contatos = new ArrayList<>();

        String sql = "SELECT u.CPF, u.nome, u.foto, "
                + "(SELECT COUNT(*) FROM Mensagens m "
                + "WHERE m.CPF_remetente = u.CPF AND m.CPF_destinatario = ? AND m.lida = FALSE) AS nao_lidas "
                + "FROM Usuarios u "
                + "WHERE u.CPF <> ? AND u.CPF IN (" + CONTATOS + ") "
                + "ORDER BY u.nome";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, cpfUsuario);
            stmt.setString(3, cpfUsuario);
            stmt.setString(4, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ContatoChat c = new ContatoChat();
                    c.setCpf(rs.getString("CPF"));
                    c.setNome(rs.getString("nome"));
                    c.setFoto(rs.getBytes("foto"));
                    c.setNaoLidas(rs.getInt("nao_lidas"));
                    contatos.add(c);
                }
            }
        }

        return contatos;
    }

    // =========================================================
    // ENVIAR MENSAGEM
    // =========================================================

    public void enviar(String cpfRemetente, String cpfDestinatario, String texto) throws SQLException {

        if (!podeConversar(cpfRemetente, cpfDestinatario)) {
            throw new SQLException("Vocês não têm mais uma solicitação em comum.");
        }

        String sql = "INSERT INTO Mensagens (CPF_remetente, CPF_destinatario, texto) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfRemetente);
            stmt.setString(2, cpfDestinatario);
            stmt.setString(3, texto);

            stmt.executeUpdate();
        }
    }

    // =========================================================
    // CONVERSA ENTRE DUAS PESSOAS
    // =========================================================

    public List<Mensagem> listarConversa(String cpfA, String cpfB) throws SQLException {

        return listarNovas(cpfA, cpfB, 0);
    }

    // Só as mensagens que chegaram depois da última já mostrada na tela
    public List<Mensagem> listarNovas(String cpfA, String cpfB, int ultimoId) throws SQLException {

        List<Mensagem> mensagens = new ArrayList<>();

        String sql = "SELECT id_mensagem, CPF_remetente, CPF_destinatario, texto, data_hora, lida "
                + "FROM Mensagens "
                + "WHERE id_mensagem > ? "
                + "AND ((CPF_remetente = ? AND CPF_destinatario = ?) "
                + "OR (CPF_remetente = ? AND CPF_destinatario = ?)) "
                + "ORDER BY id_mensagem";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setInt(1, ultimoId);
            stmt.setString(2, cpfA);
            stmt.setString(3, cpfB);
            stmt.setString(4, cpfB);
            stmt.setString(5, cpfA);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Mensagem m = new Mensagem();
                    m.setIdMensagem(rs.getInt("id_mensagem"));
                    m.setCpfRemetente(rs.getString("CPF_remetente"));
                    m.setCpfDestinatario(rs.getString("CPF_destinatario"));
                    m.setTexto(rs.getString("texto"));
                    m.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                    m.setLida(rs.getBoolean("lida"));
                    mensagens.add(m);
                }
            }
        }

        return mensagens;
    }

    // =========================================================
    // MENSAGENS NÃO LIDAS
    // =========================================================

    // O destinatário abriu a conversa
    public void marcarComoLidas(String cpfRemetente, String cpfDestinatario) throws SQLException {

        String sql = "UPDATE Mensagens SET lida = TRUE "
                + "WHERE CPF_remetente = ? AND CPF_destinatario = ? AND lida = FALSE";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfRemetente);
            stmt.setString(2, cpfDestinatario);

            stmt.executeUpdate();
        }
    }

    // Só conta quem ainda é contato: de outra pessoa a conversa não abre,
    // e o contador nunca zeraria
    public int contarNaoLidas(String cpfUsuario) throws SQLException {

        String sql = "SELECT COUNT(*) FROM Mensagens "
                + "WHERE CPF_destinatario = ? AND lida = FALSE "
                + "AND CPF_remetente IN (" + CONTATOS + ")";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, cpfUsuario);
            stmt.setString(3, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
