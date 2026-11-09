package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/capasvivas";
    private static final String USUARIO = "root";
    private static final String SENHA = "admin";

    private static Connection conn;

    public static Connection getConnection() throws SQLException {

        if (conn == null || conn.isClosed()) {

            conn = novaConexao();
        }

        return conn;
    }

    // Conexão separada da compartilhada; quem pede é quem fecha
    public static Connection novaConexao() throws SQLException {

        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}