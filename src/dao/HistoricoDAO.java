package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.RegistroHistorico;

// Guarda os empréstimos do usuário (livros que emprestou e que pegou emprestado).
// Não tem chave estrangeira para Livros porque o livro removido some da tabela
// e o histórico precisa continuar mostrando ele.
public class HistoricoDAO {

    private static boolean tabelaVerificada = false;

    // Sempre devolve uma conexão aberta
    private Connection getConn() throws SQLException {

        Connection conn = ConnectionFactory.getConnection();

        // Cria a tabela se o banco foi montado antes dela existir
        if (!tabelaVerificada) {

            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS Historico ("
                        + "id_historico INT PRIMARY KEY NOT NULL AUTO_INCREMENT, "
                        + "CPF_usuario VARCHAR(11) NOT NULL, "
                        + "tipo VARCHAR(30) NOT NULL, "
                        + "ISBN VARCHAR(13), "
                        + "titulo VARCHAR(50), "
                        + "detalhe VARCHAR(150), "
                        + "data_hora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            }

            tabelaVerificada = true;
        }

        return conn;
    }

    // =========================================================
    // REGISTRAR UMA AÇÃO
    // =========================================================

    public void registrar(String cpfUsuario, String tipo, String isbn,
                          String titulo, String detalhe) throws SQLException {

        String sql = "INSERT INTO Historico (CPF_usuario, tipo, ISBN, titulo, detalhe) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);
            stmt.setString(2, tipo);
            stmt.setString(3, isbn);
            stmt.setString(4, titulo);
            stmt.setString(5, detalhe);

            stmt.executeUpdate();
        }
    }

    // O histórico é um extra: se falhar, a ação principal não pode ser desfeita
    public void registrarSemFalhar(String cpfUsuario, String tipo, String isbn,
                                   String titulo, String detalhe) {

        try {
            registrar(cpfUsuario, tipo, isbn, titulo, detalhe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // LISTAR O HISTÓRICO DO USUÁRIO (MAIS RECENTE PRIMEIRO)
    // =========================================================

    public List<RegistroHistorico> listarPorUsuario(String cpfUsuario) throws SQLException {

        List<RegistroHistorico> registros = new ArrayList<>();

        String sql = "SELECT id_historico, tipo, ISBN, titulo, detalhe, data_hora "
                + "FROM Historico WHERE CPF_usuario = ? "
                + "AND tipo IN ('" + RegistroHistorico.EMPRESTIMO_CONCEDIDO + "', '"
                + RegistroHistorico.EMPRESTIMO_RECEBIDO + "') "
                + "ORDER BY data_hora DESC, id_historico DESC";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpfUsuario);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    RegistroHistorico r = new RegistroHistorico();
                    r.setId(rs.getInt("id_historico"));
                    r.setTipo(rs.getString("tipo"));
                    r.setIsbn(rs.getString("ISBN"));
                    r.setTituloLivro(rs.getString("titulo"));
                    r.setDetalhe(rs.getString("detalhe"));
                    r.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
                    registros.add(r);
                }
            }
        }

        return registros;
    }

    // =========================================================
    // QUANDO O USUÁRIO MUDA O CPF OU EXCLUI A CONTA
    // =========================================================

    public void trocarCpf(String cpfAtual, String cpfNovo) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "UPDATE Historico SET CPF_usuario = ? WHERE CPF_usuario = ?")) {
            stmt.setString(1, cpfNovo);
            stmt.setString(2, cpfAtual);
            stmt.executeUpdate();
        }
    }

    public void excluirDoUsuario(String cpfUsuario) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "DELETE FROM Historico WHERE CPF_usuario = ?")) {
            stmt.setString(1, cpfUsuario);
            stmt.executeUpdate();
        }
    }
}
