package Main;

import controller.LivroController;
import dao.ConnectionFactory;
import dao.LivroDAO;
import model.LivroTableModel;

import java.awt.AWTEvent;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.Connection;

import javax.swing.ImageIcon;
import java.sql.SQLException;

import View.Cadastro_Livro;

public class Main {

    public static void main(String[] args) {

        usarLogoComoIcone();

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

    // Troca o ícone do Java pela logo do sistema na barra de título e na barra
    // de tarefas de TODAS as janelas (telas e caixas de mensagem) quando abrirem
    private static void usarLogoComoIcone() {

        URL url = Main.class.getResource("/imagens/icone.png");
        if (url == null) {
            return;
        }

        Image icone = new ImageIcon(url).getImage();

        Toolkit.getDefaultToolkit().addAWTEventListener(evento -> {
            if (evento.getID() == WindowEvent.WINDOW_OPENED) {
                ((Window) evento.getSource()).setIconImage(icone);
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }
}
