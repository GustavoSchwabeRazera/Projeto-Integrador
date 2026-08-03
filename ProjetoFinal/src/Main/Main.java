package Main;

import controller.LivroController;
import model.LivroTableModel;
import View.Cadastro;

public class Main {

    public static void main(String[] args) {
        
        
        LivroTableModel model = new LivroTableModel();
        
        Cadastro view = new Cadastro();
    
        LivroController controller = new LivroController(model, view);
        
        view.setVisible(true);

    }

}