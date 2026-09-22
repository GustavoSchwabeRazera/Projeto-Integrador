package dao;

import model.Livro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class LivroDAO {

    private final Connection connection;


    public LivroDAO(Connection connection) {
        this.connection = connection;
    }


    // =========================================================
    // CREATE - INSERIR LIVRO
    // =========================================================

    public void inserir(Livro livro)
            throws SQLException {

        String sql =
                "INSERT INTO livro "
                + "(nome, editora, ano_lancamento, autor, genero) "
                + "VALUES (?, ?, ?, ?, ?)";


        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    livro.getNome()
            );

            stmt.setString(
                    2,
                    livro.getEditora()
            );

            stmt.setInt(
                    3,
                    livro.getAnoLancamento()
            );

            /*
             * Atualmente o Livro não está recebendo
             * um autor diretamente.
             *
             * Por isso o campo fica NULL.
             *
             * Quando você me mandar o relacionamento
             * Livro <-> Autor, podemos corrigir essa
             * parte para salvar os autores selecionados.
             */
            stmt.setNull(
                    4,
                    Types.INTEGER
            );

            stmt.setString(
                    5,
                    livro.getGenero()
            );

            stmt.executeUpdate();
        }
    }


    // =========================================================
    // READ - LISTAR TODOS
    // =========================================================

    public List<Livro> listar()
            throws SQLException {

        List<Livro> livros =
                new ArrayList<>();


        String sql =
                "SELECT * FROM livro";


        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                Livro livro =
                        new Livro(
                                rs.getString("nome"),
                                rs.getString("editora"),
                                rs.getInt("ano_lancamento"),
                                rs.getString("genero")
                        );

                livros.add(livro);
            }
        }


        return livros;
    }


    // =========================================================
    // READ - BUSCAR POR NOME
    // =========================================================

    public Livro buscarPorNome(
            String nome)
            throws SQLException {

        String sql =
                "SELECT * "
                + "FROM livro "
                + "WHERE nome = ?";


        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    nome
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    return new Livro(
                            rs.getString("nome"),
                            rs.getString("editora"),
                            rs.getInt("ano_lancamento"),
                            rs.getString("genero")
                    );
                }
            }
        }


        return null;
    }


    // =========================================================
    // UPDATE - ATUALIZAR LIVRO
    // =========================================================

    public void atualizar(Livro livro)
            throws SQLException {

        /*
         * Aqui estou usando o nome para localizar
         * o livro, porque seu objeto Livro atualmente
         * não mostrou um getId().
         */

        String sql =
                "UPDATE livro SET "
                + "editora = ?, "
                + "ano_lancamento = ?, "
                + "genero = ? "
                + "WHERE nome = ?";


        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    livro.getEditora()
            );

            stmt.setInt(
                    2,
                    livro.getAnoLancamento()
            );

            stmt.setString(
                    3,
                    livro.getGenero()
            );

            stmt.setString(
                    4,
                    livro.getNome()
            );

            stmt.executeUpdate();
        }
    }


    // =========================================================
    // DELETE - EXCLUIR LIVRO
    // =========================================================

    public void excluir(String nome)
            throws SQLException {

        String sql =
                "DELETE FROM livro "
                + "WHERE nome = ?";


        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    nome
            );

            stmt.executeUpdate();
        }
    }


    // =========================================================
    // BUSCAR LIVROS POR NOME
    // =========================================================

    public List<Livro> buscarLivrosPorNome(
            String texto)
            throws SQLException {

        List<Livro> livros =
                new ArrayList<>();


        String sql =
                "SELECT * "
                + "FROM livro "
                + "WHERE nome LIKE ?";


        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    "%" + texto + "%"
            );


            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                while (rs.next()) {

                    Livro livro =
                            new Livro(
                                    rs.getString("nome"),
                                    rs.getString("editora"),
                                    rs.getInt("ano_lancamento"),
                                    rs.getString("genero")
                            );

                    livros.add(livro);
                }
            }
        }


        return livros;
    }
}
