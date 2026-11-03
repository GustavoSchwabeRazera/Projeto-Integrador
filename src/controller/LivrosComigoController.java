package controller;

import java.sql.SQLException;


import View.JanelaMensagem;
import View.TelaLivrosComigo;

import dao.EmprestimoDAO;

import model.EmprestimoAtivo;

public class LivrosComigoController {

    private final EmprestimoDAO emprestimoDAO;
    private final TelaLivrosComigo telaLivrosComigo;

    // Usuário cujos livros estão na tela
    private String cpfUsuario;


    public LivrosComigoController(
            EmprestimoDAO emprestimoDAO,
            TelaLivrosComigo telaLivrosComigo) {

        this.emprestimoDAO = emprestimoDAO;
        this.telaLivrosComigo = telaLivrosComigo;

        telaLivrosComigo.setAoDevolver(this::devolver);
    }


    // =========================================================
    // LIVROS COMIGO
    // =========================================================

    public void carregarLivros(String cpfUsuario) {

        this.cpfUsuario = cpfUsuario;

        carregarLivros();
    }


    private void carregarLivros() {

        try {

            telaLivrosComigo.mostrarLivros(
                    emprestimoDAO.listarLivrosComigo(cpfUsuario)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar os livros:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // DEVOLVER
    // O empréstimo só termina quando o dono confirmar
    // =========================================================

    private void devolver(EmprestimoAtivo emprestimo) {

        if (!JanelaMensagem.confirmar(
                "Você devolveu \"" + emprestimo.getTituloLivro()
                + "\" para " + emprestimo.getNomeDono() + "?",
                "Devolver livro")) {
            return;
        }

        try {

            emprestimoDAO.solicitarDevolucao(emprestimo.getIdEmprestimo());

            mostrarMensagem(
                    "Devolução enviada!\n"
                    + emprestimo.getNomeDono()
                    + " precisa confirmar que recebeu o livro."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao devolver o livro:\n" + e.getMessage()
            );
        }

        carregarLivros();
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }
}
