package Main;

import controller.LivroController;
import model.LivroTableModel;
import View.Cadastro_Livro;

public class Main {

    public static void main(String[] args) {

        LivroTableModel model = new LivroTableModel();

        Cadastro_Livro view = new Cadastro_Livro();

        LivroController controller = new LivroController(model, view);

        controller.iniciar();
    }
}
