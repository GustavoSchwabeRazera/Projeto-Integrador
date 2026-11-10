package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


import View.JanelaMensagem;
import View.PesquisarLivro;

import dao.EmprestimoDAO;
import dao.LivroDAO;
import dao.UsuarioDAO;

import model.Livro;
import model.Solicitacao;

public class PesquisarController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

            LocalDate[] periodo = pedirPeriodo(livro);

            // Cancelou
            if (periodo == null) {
                return;
            }

            emprestimoDAO.solicitar(cpfUsuario, livro.getIsbn(), periodo[0], periodo[1]);

            mostrarMensagem(
                    "Solicitação de empréstimo de \"" + livro.getNome() + "\" enviada!\n"
                    + "Retirada: " + periodo[0].format(FORMATO_DATA) + "\n"
                    + "Devolução: " + periodo[1].format(FORMATO_DATA) + "\n"
                    + "As datas já estão no seu calendário. Aguarde o dono do livro aceitar."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao solicitar empréstimo:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // DIAS DE RETIRADA E DEVOLUÇÃO
    // Pergunta de novo até as datas estarem certas.
    // Devolve { retirada, devolução } ou null se cancelou
    // =========================================================

    private LocalDate[] pedirPeriodo(Livro livro) {

        LocalDate hoje = LocalDate.now();
        LocalDate ultimaRetirada = hoje.plusDays(Solicitacao.MAX_DIAS_ATE_RETIRADA);

        LocalDate retirada = hoje;
        LocalDate retorno = hoje.plusDays(EmprestimoDAO.DIAS_EMPRESTIMO);

        while (true) {

            LocalDate[] periodo = JanelaMensagem.pedirPeriodo(
                    "Escolha quando vai pegar \"" + livro.getNome() + "\" e quando vai devolver ao dono.\n"
                    + "A retirada pode ser até " + ultimaRetirada.format(FORMATO_DATA)
                    + " e o empréstimo dura no máximo "
                    + Solicitacao.MAX_DIAS_EMPRESTIMO + " dias.",
                    "Datas do empréstimo",
                    retirada,
                    retorno,
                    hoje,
                    ultimaRetirada,
                    Solicitacao.MAX_DIAS_EMPRESTIMO);

            if (periodo == null) {
                return null;
            }

            retirada = periodo[0];
            retorno = periodo[1];

            String erro = validarPeriodo(retirada, retorno, hoje, ultimaRetirada);

            if (erro == null) {
                return periodo;
            }

            mostrarMensagem(erro);
        }
    }

    private String validarPeriodo(LocalDate retirada, LocalDate retorno,
            LocalDate hoje, LocalDate ultimaRetirada) {

        if (retirada == null || retorno == null) {
            return "Escolha as duas datas (dd/mm/aaaa).";
        }

        if (retirada.isBefore(hoje)) {
            return "O dia de retirada não pode ser antes de hoje.";
        }

        if (retirada.isAfter(ultimaRetirada)) {
            return "O dia de retirada deve ser até " + ultimaRetirada.format(FORMATO_DATA) + ".";
        }

        if (!retorno.isAfter(retirada)) {
            return "O dia de devolução deve ser depois do dia de retirada.";
        }

        if (retorno.isAfter(retirada.plusDays(Solicitacao.MAX_DIAS_EMPRESTIMO))) {
            return "O empréstimo pode durar no máximo "
                    + Solicitacao.MAX_DIAS_EMPRESTIMO + " dias.\n"
                    + "Devolva até " + retirada.plusDays(Solicitacao.MAX_DIAS_EMPRESTIMO)
                            .format(FORMATO_DATA) + ".";
        }

        return null;
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }
}
