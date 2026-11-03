package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Autor;

public class AutorDAO {

    private Connection conexao;

    public AutorDAO(Connection conexao) {
        this.conexao = conexao;
    }

    // Cadastrar autor
    public void cadastrar(Autor autor) throws SQLException {

        String sql =
                "INSERT INTO Autor (nome, nacionalidade) VALUES (?, ?)";

        PreparedStatement stmt = null;

        try {

            stmt = conexao.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());

            stmt.executeUpdate();

            // Pega o ID gerado pelo banco
            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                autor.setId_autor(rs.getInt(1));
            }

            rs.close();

        } finally {

            if (stmt != null) {
                stmt.close();
            }
        }
    }

    // Alterar autor
    public void atualizar(Autor autor) throws SQLException {

        String sql =
                "UPDATE Autor SET nome = ?, nacionalidade = ? WHERE id_autor = ?";

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {

            stmt.setString(1, autor.getNome());
            stmt.setString(2, autor.getNacionalidade());
            stmt.setInt(3, autor.getId_autor());

            stmt.executeUpdate();
        }
    }

    // Excluir autor
    public void excluir(int idAutor) throws SQLException {

        try (PreparedStatement stmt = conexao.prepareStatement(
                "DELETE FROM Autor WHERE id_autor = ?")) {

            stmt.setInt(1, idAutor);

            stmt.executeUpdate();
        }
    }

    // Verifica se o autor está ligado a algum livro
    public boolean temLivros(int idAutor) throws SQLException {

        try (PreparedStatement stmt = conexao.prepareStatement(
                "SELECT 1 FROM Pertence WHERE id_autor = ? LIMIT 1")) {

            stmt.setInt(1, idAutor);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Listar todos os autores
    public List<Autor> listarAutores() throws SQLException {

        List<Autor> autores = new ArrayList<>();

        String sql =
                "SELECT id_autor, nome, nacionalidade " +
                "FROM Autor " +
                "ORDER BY nome";

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            stmt = conexao.prepareStatement(sql);

            rs = stmt.executeQuery();

            while (rs.next()) {

                Autor autor = new Autor();

                autor.setId_autor(
                        rs.getInt("id_autor")
                );

                autor.setNome(
                        rs.getString("nome")
                );

                autor.setNacionalidade(
                        rs.getString("nacionalidade")
                );

                autores.add(autor);
            }

        } finally {

            if (rs != null) {
                rs.close();
            }

            if (stmt != null) {
                stmt.close();
            }
        }

        return autores;
    }
}