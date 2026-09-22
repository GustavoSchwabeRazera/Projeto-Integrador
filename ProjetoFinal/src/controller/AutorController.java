package controller;

import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import View.Cadastro_Autor;
import dao.AutorDAO;
import dao.ConnectionFactory;
import model.Autor;

public class AutorController {

    private Cadastro_Autor cadastroAutor;
    private AutorDAO autorDAO;

    public AutorController(Cadastro_Autor cadastroAutor) {

        this.cadastroAutor = cadastroAutor;

        try {

            Connection conexao = ConnectionFactory.getConnection();

            this.autorDAO = new AutorDAO(conexao);

        } catch (SQLException e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao conectar ao banco de dados."
            );
        }

        configurarEventos();
    }

    private void configurarEventos() {

        cadastroAutor.getBtnAdicionar()
                .addActionListener(e -> cadastrarAutor());
    }

    private void cadastrarAutor() {

        String nome =
                cadastroAutor.getTxtNome()
                .getText()
                .trim();

        String nacionalidade =
                cadastroAutor.getComboBox()
                .getSelectedItem()
                .toString();

        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    null,
                    "Preencha o nome do autor."
            );

            return;
        }

        try {

            Autor autor = new Autor();

            autor.setNome(nome);
            autor.setNacionalidade(nacionalidade);

            autorDAO.cadastrar(autor);

            JOptionPane.showMessageDialog(
                    null,
                    "Autor cadastrado com sucesso!"
            );

            cadastroAutor.getTxtNome().setText("");

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    null,
                    "Erro ao cadastrar o autor."
            );
        }
    }
}