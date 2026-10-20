package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class UsuarioDAO {
	

    public void cadastrar(String cpf, String nome, String telefone,
                          String email, String senha, String dataNascimento)
            throws SQLException {

        String sql = "INSERT INTO Usuarios "
                   + "(CPF, nome, telefone, email, senha, data_nascimento) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, nome);
            stmt.setString(3, telefone);
            stmt.setString(4, email);
            stmt.setString(5, senha);

            // Converte de dd/MM/yyyy para yyyy-MM-dd
            String[] partes = dataNascimento.split("/");

            String dataMySQL = partes[2] + "-"
                             + partes[1] + "-"
                             + partes[0];

            stmt.setString(6, dataMySQL);

            stmt.executeUpdate();
        }
    }
    public boolean login(String email, String senha) throws SQLException {

        String sql = "SELECT * FROM Usuarios WHERE email = ? AND senha = ?";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, senha);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
    
    public String[] buscarUsuarioPorEmail(String email) throws SQLException {

        String sql = "SELECT nome, email, telefone, data_nascimento "
                   + "FROM Usuarios WHERE email = ?";

        Connection conn = ConnectionFactory.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String dataNascimento = "";

                    if (rs.getDate("data_nascimento") != null) {
                        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
                        dataNascimento = formato.format(rs.getDate("data_nascimento"));
                    }

                    return new String[] {
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        dataNascimento
                    };
                }
            }
        }

        return null;
    }
    
    
    public void atualizarFoto(String cpf, byte[] foto) throws SQLException {

        String sql = "UPDATE Usuarios SET foto = ? WHERE CPF = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBytes(1, foto);
            stmt.setString(2, cpf);

            stmt.executeUpdate();
        }
    }
    
    public byte[] buscarFoto(String cpf) throws SQLException {

        String sql = "SELECT foto FROM Usuarios WHERE CPF = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBytes("foto");
            }
        }

        return null;
    }
    
    public String buscarCpfPorEmail(String email) throws SQLException {

        String sql = "SELECT CPF FROM Usuarios WHERE email = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getString("CPF");
                }
            }
        }

        return null;
    }
    
    // Dados públicos do usuário (sem senha), usados para mostrar quem cadastrou um livro
    public String[] buscarContatoPorCpf(String cpf) throws SQLException {

        String sql = "SELECT nome, email, telefone FROM Usuarios WHERE CPF = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new String[] {
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone")
                    };
                }
            }
        }

        return null;
    }

    public String[] buscarDadosParaAlteracao(String cpf) throws SQLException {

        String sql = "SELECT CPF, nome, email, telefone, data_nascimento, senha "
                   + "FROM Usuarios WHERE CPF = ?";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    String dataNascimento = "";

                    if (rs.getDate("data_nascimento") != null) {
                        SimpleDateFormat formato =
                                new SimpleDateFormat("dd/MM/yyyy");

                        dataNascimento =
                                formato.format(rs.getDate("data_nascimento"));
                    }

                    return new String[] {
                        rs.getString("CPF"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getString("telefone"),
                        dataNascimento,
                        rs.getString("senha")
                    };
                }
            }
        }

        return null;
    }
    public void atualizarCadastro(String cpfAtual, String cpfNovo,
            String nome, String telefone,
            String email, String senha,
            String dataNascimento) throws SQLException {

String sql = "UPDATE Usuarios SET "
+ "CPF = ?, "
+ "nome = ?, "
+ "telefone = ?, "
+ "email = ?, "
+ "senha = ?, "
+ "data_nascimento = ? "
+ "WHERE CPF = ?";

try (Connection conn = ConnectionFactory.getConnection();
PreparedStatement stmt = conn.prepareStatement(sql)) {

stmt.setString(1, cpfNovo);
stmt.setString(2, nome);
stmt.setString(3, telefone);
stmt.setString(4, email);
stmt.setString(5, senha);

// Converte dd/MM/yyyy para yyyy-MM-dd
String[] partes = dataNascimento.split("/");

String dataMySQL = partes[2] + "-"
       + partes[1] + "-"
       + partes[0];

stmt.setString(6, dataMySQL);

// CPF que identifica o registro atualmente
stmt.setString(7, cpfAtual);

stmt.executeUpdate();
}

// O histórico acompanha o CPF novo
if (!cpfNovo.equals(cpfAtual)) {
    new HistoricoDAO().trocarCpf(cpfAtual, cpfNovo);
}
}

    // Exclui o usuário e tudo que depende dele (empréstimos, livros e autores dos livros)
    public void excluirConta(String cpf) throws SQLException {

        // A ordem importa por causa das chaves estrangeiras
        String[] sqls = {
            "DELETE FROM Eventos WHERE CPF_usuario = ?",
            "DELETE FROM Solicitacoes WHERE CPF_solicitante = ? "
                + "OR ISBN IN (SELECT ISBN FROM Livros WHERE CPF_dono = ?)",
            // Livros que o usuário estava lendo voltam a ficar disponíveis (trigger)
            "UPDATE Emprestimo SET data_devolucao = CURDATE() "
                + "WHERE CPF_leitor = ? AND data_devolucao IS NULL",
            "DELETE FROM Emprestimo WHERE CPF_leitor = ? OR CPF_dono = ?",
            "DELETE FROM Pertence WHERE ISBN IN "
                + "(SELECT ISBN FROM Livros WHERE CPF_dono = ?)",
            "DELETE FROM Livros WHERE CPF_dono = ?",
            "DELETE FROM Usuarios WHERE CPF = ?"
        };

        try (Connection conn = ConnectionFactory.getConnection()) {

            // Tudo ou nada: se um DELETE falhar, desfaz os anteriores
            conn.setAutoCommit(false);

            try {

                for (String sql : sqls) {

                    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                        // Todos os parâmetros são o CPF do usuário
                        int parametros = sql.length() - sql.replace("?", "").length();
                        for (int i = 1; i <= parametros; i++) {
                            stmt.setString(i, cpf);
                        }

                        stmt.executeUpdate();
                    }
                }

                conn.commit();

            } catch (SQLException e) {

                conn.rollback();
                throw e;

            } finally {

                conn.setAutoCommit(true);
            }
        }

        new HistoricoDAO().excluirDoUsuario(cpf);
    }

    
    
}