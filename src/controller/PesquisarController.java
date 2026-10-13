package controller;

import java.awt.Color;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.PesquisarLivro;

import dao.LivroDAO;

import model.Livro;

public class PesquisarController {

    private final LivroDAO livroDAO;
    private final PesquisarLivro pesquisarLivro;


    public PesquisarController(
            LivroDAO livroDAO,
            PesquisarLivro pesquisarLivro) {

        this.livroDAO = livroDAO;
        this.pesquisarLivro = pesquisarLivro;

        pesquisarLivro.getBtnPesquisar()
                .addActionListener(e -> pesquisar());
    }


    // =========================================================
    // PESQUISAR LIVRO
    // =========================================================

    public void pesquisar() {

        String texto =
                pesquisarLivro.getTextoPesquisa().toLowerCase();

        try {

            // Busca todos os livros do banco
            List<Livro> listaLivros =
                    livroDAO.listar();

            // Se digitou algo, filtra pelo título
            if (!texto.isEmpty()) {
                listaLivros.removeIf(livro ->
                        livro.getNome() == null
                        || !livro.getNome().toLowerCase().contains(texto));
            }

            pesquisarLivro.mostrarLivros(listaLivros);

            if (listaLivros.isEmpty()) {

                mostrarMensagem(
                        "Nenhum livro encontrado."
                );
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao pesquisar livros:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        UIManager.put(
                "OptionPane.background",
                new Color(175, 244, 198)
        );

        UIManager.put(
                "Panel.background",
                new Color(175, 244, 198)
        );


        JOptionPane.showMessageDialog(
                null,
                mensagem
        );


        UIManager.put(
                "OptionPane.background",
                null
        );

        UIManager.put(
                "Panel.background",
                null
        );
    }
}
