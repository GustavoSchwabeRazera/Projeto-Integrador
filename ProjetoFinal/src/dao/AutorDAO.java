package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import model.Autor;

public class AutorDAO {

    private Connection conexao;

    public AutorDAO(Connection conexao) {
        this.conexao = conexao;
    }

    public void cadastrar(Autor autor) {

        String sql = "INSERT INTO Autor (id_autor, nome, nacionalidade) VALUES (?, ?, ?)";

        try {

            PreparedStatement stmt = conexao.prepareStatement(sql);

            stmt.setInt(1, autor.getId_autor());
            stmt.setString(2, autor.getNome());
            stmt.setString(3, autor.getNacionalidade());

            stmt.executeUpdate();

            stmt.close();

            System.out.println("Autor cadastrado com sucesso!");

        } catch (SQLException e) {

            System.out.println("Erro ao cadastrar autor:");
            e.printStackTrace();
        }
    }
}
