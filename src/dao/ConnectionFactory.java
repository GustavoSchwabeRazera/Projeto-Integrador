package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection conn;

    public static Connection getConnection() throws SQLException {

        if (conn == null || conn.isClosed()) {

            String url = "jdbc:mysql://localhost:3306/capasvivas";
            String usuario = "root";
            String senha = "admin";

            conn = DriverManager.getConnection(url, usuario, senha);
        }

        return conn;
    }
}