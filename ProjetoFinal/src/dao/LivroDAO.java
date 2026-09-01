package dao;

import model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    // CREATE - Inserir livro
    public void inserir(Livro livro) throws SQLException {

        String sql = "INSERT INTO livro " +
                     "(nome, editora, ano_lancamento, autor, genero) " +
                     "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, livro.getNome());
        stmt.setString(2, livro.getEditora());
        stmt.setInt(3, livro.getAnoLancamento());
        stmt.setString(4, livro.getAutor());
        stmt.setString(5, livro.getGenero());

        stmt.executeUpdate();

        stmt.close();
    }

    // READ - Listar todos os livros
    public List<Livro> listar() throws SQLException {

        List<Livro> livros = new ArrayList<>();

        String sql = "SELECT * FROM livro";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Livro livro = new Livro(
                rs.getString("nome"),
                rs.getString("editora"),
                rs.getInt("ano_lancamento"),
                rs.getString("autor"),
                rs.getString("genero")
            );

            livros.add(livro);
        }

        rs.close();
        stmt.close();

        return livros;
    }

    // READ - Buscar livro pelo nome
    public Livro buscarPorNome(String nome) throws SQLException {

        String sql = "SELECT * FROM livro WHERE nome = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, nome);

        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {

            Livro livro = new Livro(
                rs.getString("nome"),
                rs.getString("editora"),
                rs.getInt("ano_lancamento"),
                rs.getString("autor"),
                rs.getString("genero")
            );

            rs.close();
            stmt.close();

            return livro;
        }

        rs.close();
        stmt.close();

        return null;
    }

    // UPDATE - Atualizar livro
    public void atualizar(Livro livro) throws SQLException {

        String sql = "UPDATE livro SET " +
                     "editora = ?, " +
                     "ano_lancamento = ?, " +
                     "autor = ?, " +
                     "genero = ? " +
                     "WHERE id = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, livro.getEditora());
        stmt.setInt(2, livro.getAnoLancamento());
        stmt.setString(3, livro.getAutor());
        stmt.setString(4, livro.getGenero());
        stmt.setString(5, livro.getNome());

        stmt.executeUpdate();

        stmt.close();
    }

    // DELETE - Excluir livro
    public void excluir(String nome) throws SQLException {

        String sql = "DELETE FROM livro WHERE nome = ?";

        PreparedStatement stmt = connection.prepareStatement(sql);

        stmt.setString(1, nome);

        stmt.executeUpdate();

        stmt.close();
    }

	public List<Livro> buscarLivrosPorNome(String texto) throws SQLException {
		   List<Livro> livros = new ArrayList<>();

	        String sql = "SELECT * FROM livro where nome like '%?%'";

	        PreparedStatement stmt = connection.prepareStatement(sql);
	        stmt.setString(1, texto);
	        ResultSet rs = stmt.executeQuery();

	        while (rs.next()) {

	            Livro livro = new Livro(
	                rs.getString("nome"),
	                rs.getString("editora"),
	                rs.getInt("ano_lancamento"),
	                rs.getString("autor"),
	                rs.getString("genero")
	            );

	            livros.add(livro);
	        }

	        rs.close();
	        stmt.close();

	        return livros;
	}
    
    
    
}
