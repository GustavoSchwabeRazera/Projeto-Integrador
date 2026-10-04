package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Autor;
import model.Livro;

public class LivroDAO {

    public LivroDAO(Connection connection) {
        // A conexão é pega na hora pelo getConn(),
        // porque outras partes do programa podem fechar a antiga
    }

    // Sempre devolve uma conexão aberta
    private Connection getConn() throws SQLException {
        return ConnectionFactory.getConnection();
    }

    // =========================================================
    // CADASTRAR LIVRO + AUTORES
    // =========================================================

    public void inserir(Livro livro) throws SQLException {

        String sqlLivro = "INSERT INTO Livros "
                + "(ISBN, fotoContraCapa, fotoCapa, status, CPF_dono, titulo, data_lancamento, editora, generos) "
                + "VALUES (?, NULL, ?, ?, ?, ?, ?, ?, ?)";

        String sqlPertence = "INSERT INTO Pertence (ISBN, id_autor) VALUES (?, ?)";

        Connection connection = getConn();
        boolean autoCommitAnterior = connection.getAutoCommit();

        try {
            connection.setAutoCommit(false);

            // INSERIR LIVRO
            try (PreparedStatement stmt = connection.prepareStatement(sqlLivro)) {
                stmt.setString(1, livro.getIsbn());
                stmt.setBytes(2, livro.getFotoCapa());   // foto da capa (pode ser null)
                stmt.setBoolean(3, true);
                stmt.setString(4, livro.getCpfDono());   // usuário logado
                stmt.setString(5, livro.getNome());
                stmt.setDate(6, java.sql.Date.valueOf(
                        String.format("%04d", livro.getAnoLancamento()) + "-01-01"));
                stmt.setString(7, livro.getEditora());
                stmt.setString(8, livro.getGenero());
                stmt.executeUpdate();
            }

            // INSERIR AUTORES
            List<Autor> autores = livro.getAutores();
            if (autores != null && !autores.isEmpty()) {
                try (PreparedStatement stmt = connection.prepareStatement(sqlPertence)) {
                    for (Autor autor : autores) {
                        stmt.setString(1, livro.getIsbn());
                        stmt.setInt(2, autor.getId_autor());
                        stmt.executeUpdate();
                    }
                }
            }

            connection.commit();

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(autoCommitAnterior);
        }
    }

    // =========================================================
    // LISTAR TODOS OS LIVROS
    // =========================================================

    public List<Livro> listar() throws SQLException {

        List<Livro> livros = new ArrayList<>();

        String sql = "SELECT ISBN, titulo, editora, data_lancamento, generos "
                + "FROM Livros ORDER BY titulo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                livros.add(criarLivro(rs));
            }
        }

        return livros;
    }

    // =========================================================
    // LISTAR LIVROS DO USUÁRIO (COM FOTO E AUTORES)
    // =========================================================

    public List<Livro> listarPorDono(String cpf) throws SQLException {

        List<Livro> livros = new ArrayList<>();

        String sql = "SELECT ISBN, titulo, editora, data_lancamento, generos, fotoCapa "
                + "FROM Livros WHERE CPF_dono = ? ORDER BY titulo";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, cpf);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = criarLivro(rs);
                    livro.setFotoCapa(rs.getBytes("fotoCapa"));
                    livro.setCpfDono(cpf);
                    livros.add(livro);
                }
            }
        }

        // Busca os autores de cada livro
        for (Livro livro : livros) {
            livro.setAutores(buscarAutores(livro.getIsbn()));
        }

        return livros;
    }

    // =========================================================
    // BUSCAR AUTORES DE UM LIVRO
    // =========================================================

    public List<Autor> buscarAutores(String isbn) throws SQLException {

        List<Autor> autores = new ArrayList<>();

        String sql = "SELECT a.id_autor, a.nome, a.nacionalidade "
                + "FROM Autor a JOIN Pertence p ON p.id_autor = a.id_autor "
                + "WHERE p.ISBN = ?";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    autores.add(new Autor(
                            rs.getInt("id_autor"),
                            rs.getString("nome"),
                            rs.getString("nacionalidade")));
                }
            }
        }

        return autores;
    }

    // =========================================================
    // BUSCAR POR ISBN
    // =========================================================

    public Livro buscarPorISBN(String isbn) throws SQLException {

        String sql = "SELECT ISBN, titulo, editora, data_lancamento, generos "
                + "FROM Livros WHERE ISBN = ?";

        try (PreparedStatement stmt = getConn().prepareStatement(sql)) {

            stmt.setString(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return criarLivro(rs);
                }
            }
        }

        return null;
    }

    // =========================================================
    // EXCLUIR LIVRO
    // =========================================================

    public void excluir(String isbn) throws SQLException {

        try (PreparedStatement stmt = getConn().prepareStatement(
                "DELETE FROM Pertence WHERE ISBN = ?")) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = getConn().prepareStatement(
                "DELETE FROM Livros WHERE ISBN = ?")) {
            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    // =========================================================
    // MONTA UM LIVRO COM OS DADOS DA LINHA DO BANCO
    // =========================================================

    private Livro criarLivro(ResultSet rs) throws SQLException {

        int ano = 0;
        java.sql.Date data = rs.getDate("data_lancamento");

        if (data != null) {
            ano = data.toLocalDate().getYear();
        }

        return new Livro(
                rs.getString("ISBN"),
                rs.getString("titulo"),
                rs.getString("editora"),
                ano,
                rs.getString("generos"),
                null);
    }
}