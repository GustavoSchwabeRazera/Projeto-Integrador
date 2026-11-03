package controller;

import java.sql.SQLException;


import View.JanelaMensagem;
import View.TelaNotificacoes;

import dao.EmprestimoDAO;

import model.EmprestimoAtivo;

public class NotificacaoController {

    // Avisa quando faltar até esse número de dias para o empréstimo acabar
    private static final int DIAS_AVISO = 3;

    private final EmprestimoDAO emprestimoDAO;
    private final TelaNotificacoes telaNotificacoes;

    // Usuário cujas notificações estão na tela
    private String cpfUsuario;


    public NotificacaoController(
            EmprestimoDAO emprestimoDAO,
            TelaNotificacoes telaNotificacoes,
            SolicitacaoController solicitacaoController) {

        this.emprestimoDAO = emprestimoDAO;
        this.telaNotificacoes = telaNotificacoes;

        // Aceitar/recusar usam a mesma regra da tela de Solicitações
        telaNotificacoes.setAoAceitar(s -> {
            solicitacaoController.aceitarSolicitacao(s);
            carregarNotificacoes();
        });

        telaNotificacoes.setAoRecusar(s -> {
            solicitacaoController.recusarSolicitacao(s);
            carregarNotificacoes();
        });

        telaNotificacoes.setAoConfirmarDevolucao(this::confirmarDevolucao);

        telaNotificacoes.setAoRecusarDevolucao(this::recusarDevolucao);
    }


    // =========================================================
    // NOTIFICAÇÕES
    // =========================================================

    public void carregarNotificacoes(String cpfUsuario) {

        this.cpfUsuario = cpfUsuario;

        carregarNotificacoes();
    }


    private void carregarNotificacoes() {

        try {

            telaNotificacoes.mostrarNotificacoes(
                    emprestimoDAO.listarAtivosPertoDoFim(cpfUsuario, DIAS_AVISO),
                    emprestimoDAO.listarDevolucoesParaConfirmar(cpfUsuario),
                    emprestimoDAO.listarPendentesDoDono(cpfUsuario)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar notificações:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // DEVOLUÇÕES
    // =========================================================

    // O trigger do banco deixa o livro disponível de novo
    private void confirmarDevolucao(EmprestimoAtivo devolucao) {

        if (!JanelaMensagem.confirmar(
                "Você recebeu \"" + devolucao.getTituloLivro()
                + "\" de " + devolucao.getNomeLeitor() + "?",
                "Confirmar devolução")) {
            return;
        }

        try {

            emprestimoDAO.registrarDevolucao(devolucao.getIsbn());

            mostrarMensagem(
                    "Devolução confirmada!\n\"" + devolucao.getTituloLivro()
                    + "\" está disponível de novo."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao confirmar a devolução:\n" + e.getMessage()
            );
        }

        carregarNotificacoes();
    }


    private void recusarDevolucao(EmprestimoAtivo devolucao) {

        if (!JanelaMensagem.confirmar(
                "Você ainda não recebeu \"" + devolucao.getTituloLivro()
                + "\"? O livro continua emprestado para " + devolucao.getNomeLeitor() + ".",
                "Recusar devolução")) {
            return;
        }

        try {

            emprestimoDAO.recusarDevolucao(devolucao.getIdEmprestimo());

            mostrarMensagem(
                    "Devolução recusada."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao recusar a devolução:\n" + e.getMessage()
            );
        }

        carregarNotificacoes();
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }
}
