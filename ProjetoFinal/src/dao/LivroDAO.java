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

    private final Connection connection;

    public LivroDAO(Connection connection) {
        this.connection = connection;
    }

    // =========================================================
    // CADASTRAR LIVRO + AUTORES
    // =========================================================

    public void inserir(
            Livro livro
            ) throws SQLException {

        String sqlLivro =
                "INSERT INTO Livros "
                + "(ISBN, fotoContraCapa, fotoCapa, status, "
                + "CPF_dono, titulo, data_lancamento, editora, generos) "
                + "VALUES (?, NULL, NULL, ?, ?, ?, ?, ?, ?)";

        String sqlPertence =
                "INSERT INTO Pertence (ISBN, id_autor) "
                + "VALUES (?, ?)";

        boolean autoCommitAnterior =
                connection.getAutoCommit();

        try {

            connection.setAutoCommit(false);

            // =================================================
            // INSERIR LIVRO
            // =================================================

            try (PreparedStatement stmt =
                    connection.prepareStatement(sqlLivro)) {

                // ISBN
                stmt.setString(
                        1,
                        livro.getIsbn()
                );

                // STATUS
                stmt.setBoolean(
                        2,
                        true
                );

                // CPF DO DONO
                // Temporário até integrar com o login
                stmt.setString(
                        3,
                        "12345678911"
                );

                // TÍTULO
                stmt.setString(
                        4,
                        livro.getNome()
                );

                // DATA DE LANÇAMENTO
                stmt.setDate(
                        5,
                        java.sql.Date.valueOf(
                                livro.getAnoLancamento()
                                        + "-01-01"
                        )
                );

                // EDITORA
                stmt.setString(
                        6,
                        livro.getEditora()
                );

                // GÊNERO
                stmt.setString(
                        7,
                        livro.getGenero()
                );

                stmt.executeUpdate();
            }

            // =================================================
            // INSERIR AUTORES
            // =================================================

            List<Autor> autores = livro.getAutores();
            if (autores != null
                    && !autores.isEmpty()) {

                try (PreparedStatement stmt =
                        connection.prepareStatement(sqlPertence)) {

                    for (Autor autor : autores) {

                        stmt.setString(
                                1,
                                livro.getIsbn()
                        );

                        stmt.setInt(
                                2,
                                autor.getId_autor()
                        );

                        stmt.executeUpdate();
                    }
                }
            }

            // =================================================
            // CONFIRMAR
            // =================================================

            connection.commit();

        } catch (SQLException e) {

            connection.rollback();

            throw e;

        } finally {

            connection.setAutoCommit(
                    autoCommitAnterior
            );
        }
    }

    // =========================================================
    // LISTAR LIVROS
    // =========================================================

    public List<Livro> listar()
            throws SQLException {

        List<Livro> livros =
                new ArrayList<>();

        String sql =
                "SELECT ISBN, titulo, editora, "
                + "data_lancamento, generos "
                + "FROM Livros "
                + "ORDER BY titulo";

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql);

                ResultSet rs =
                        stmt.executeQuery()
        ) {

            while (rs.next()) {

                java.sql.Date data =
                        rs.getDate(
                                "data_lancamento"
                        );

                int ano = 0;

                if (data != null) {

                    ano =
                            data.toLocalDate()
                                    .getYear();
                }

                Livro livro =
                        new Livro(
                                rs.getString("ISBN"),
                                rs.getString("titulo"),
                                rs.getString("editora"),
                                ano,
                                rs.getString("generos")
                        );

                livros.add(livro);
            }
        }

        return livros;
    }

    // =========================================================
    // BUSCAR POR ISBN
    // =========================================================

    public Livro buscarPorISBN(
            String isbn) throws SQLException {

        String sql =
                "SELECT ISBN, titulo, editora, "
                + "data_lancamento, generos "
                + "FROM Livros "
                + "WHERE ISBN = ?";

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(sql)
        ) {

            stmt.setString(
                    1,
                    isbn
            );

            try (
                    ResultSet rs =
                            stmt.executeQuery()
            ) {

                if (rs.next()) {

                    java.sql.Date data =
                            rs.getDate(
                                    "data_lancamento"
                            );

                    int ano = 0;

                    if (data != null) {

                        ano =
                                data.toLocalDate()
                                        .getYear();
                    }

                    return new Livro(
                            rs.getString("ISBN"),
                            rs.getString("titulo"),
                            rs.getString("editora"),
                            ano,
                            rs.getString("generos")
                    );
                }
            }
        }

        return null;
    }

    // =========================================================
    // EXCLUIR LIVRO
    // =========================================================

    public void excluir(
            String isbn) throws SQLException {

        String sqlPertence =
                "DELETE FROM Pertence "
                + "WHERE ISBN = ?";

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(
                                sqlPertence
                        )
        ) {

            stmt.setString(
                    1,
                    isbn
            );

            stmt.executeUpdate();
        }

        String sqlLivro =
                "DELETE FROM Livros "
                + "WHERE ISBN = ?";

        try (
                PreparedStatement stmt =
                        connection.prepareStatement(
                                sqlLivro
                        )
        ) {

            stmt.setString(
                    1,
                    isbn
            );

            stmt.executeUpdate();
        }
    }
}