package controller;

import java.sql.SQLException;
import java.util.List;


import View.JanelaMensagem;
import View.PesquisarLivro;

import dao.EmprestimoDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;

import model.Livro;

public class PesquisarController {

    private final LivroDAO livroDAO;
    private final EmprestimoDAO emprestimoDAO;
    private final UsuarioDAO usuarioDAO;
    private final PesquisarLivro pesquisarLivro;


    public PesquisarController(
            LivroDAO livroDAO,
            EmprestimoDAO emprestimoDAO,
            UsuarioDAO usuarioDAO,
            PesquisarLivro pesquisarLivro) {

        this.livroDAO = livroDAO;
        this.emprestimoDAO = emprestimoDAO;
        this.usuarioDAO = usuarioDAO;
        this.pesquisarLivro = pesquisarLivro;

        pesquisarLivro.setAoSolicitar(this::solicitarEmprestimo);

        pesquisarLivro.setBuscarDono(this::buscarDono);

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
    // DADOS DO DONO DO LIVRO
    // =========================================================

    private String[] buscarDono(String cpfDono) {

        try {

            return usuarioDAO.buscarContatoPorCpf(cpfDono);

        } catch (SQLException e) {

            e.printStackTrace();

            return null;
        }
    }


    // =========================================================
    // SOLICITAR EMPRÉSTIMO
    // =========================================================

    private void solicitarEmprestimo(Livro livro) {

        String cpfUsuario = pesquisarLivro.getCpfUsuario();

        if (cpfUsuario == null) {

            mostrarMensagem(
                    "Faça login para solicitar um empréstimo."
            );

            return;
        }

        try {

            if (!emprestimoDAO.livroDisponivel(livro.getIsbn())) {

                mostrarMensagem(
                        "\"" + livro.getNome() + "\" já está emprestado no momento."
                );

                return;
            }

            if (emprestimoDAO.temEmprestimoAtivo(cpfUsuario)) {

                mostrarMensagem(
                        "Você já está com um livro emprestado.\n"
                        + "Devolva esse livro antes de solicitar outro."
                );

                return;
            }

            if (emprestimoDAO.jaSolicitou(cpfUsuario, livro.getIsbn())) {

                mostrarMensagem(
                        "Você já solicitou o empréstimo de \"" + livro.getNome() + "\"."
                );

                return;
            }

            emprestimoDAO.solicitar(cpfUsuario, livro.getIsbn());

            mostrarMensagem(
                    "Solicitação de empréstimo de \"" + livro.getNome() + "\" enviada!\n"
                    + "Aguarde o dono do livro aceitar."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao solicitar empréstimo:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }
}
