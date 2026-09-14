package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

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
}