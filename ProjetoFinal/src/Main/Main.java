package Main;

import controller.LivroController;
import dao.ConnectionFactory;
import dao.LivroDAO;
import model.LivroTableModel;

import java.sql.Connection;
import java.sql.SQLException;

import View.Cadastro_Livro;

public class Main {

    public static void main(String[] args) {
    	Connection connection = null;
		try {
			connection = ConnectionFactory.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
        LivroTableModel model = new LivroTableModel();

        Cadastro_Livro view = new Cadastro_Livro();
        LivroDAO livroDAO = new LivroDAO(connection);
        LivroController controller = new LivroController(livroDAO, model, view);

        controller.iniciar();
    }
}
