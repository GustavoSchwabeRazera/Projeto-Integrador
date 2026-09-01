package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	
	private static Connection conn;
	
    public static Connection getConnection() throws SQLException {
    	
    	if(ConnectionFactory.conn == null) {
            String url = "jdbc:mysql://localhost:3306/capasvivas";
            String usuario = "root";
            String senha = "admin";

            ConnectionFactory.conn = DriverManager.getConnection(url, usuario, senha);
    		
    	}
    	return ConnectionFactory.conn;
    }
}
