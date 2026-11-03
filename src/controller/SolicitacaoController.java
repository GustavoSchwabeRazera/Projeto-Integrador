package controller;

import java.sql.SQLException;


import View.JanelaMensagem;
import View.TelaSolicitacoes;

import dao.EmprestimoDAO;

import model.Solicitacao;

public class SolicitacaoController {

    private final EmprestimoDAO emprestimoDAO;
    private final TelaSolicitacoes telaSolicitacoes;

    // Dono dos livros cujas solicitações estão na tela
    private String cpfUsuario;


    public SolicitacaoController(
            EmprestimoDAO emprestimoDAO,
            TelaSolicitacoes telaSolicitacoes) {

        this.emprestimoDAO = emprestimoDAO;
        this.telaSolicitacoes = telaSolicitacoes;

        telaSolicitacoes.setAoAceitar(this::aceitarSolicitacao);

        telaSolicitacoes.setAoRecusar(this::recusarSolicitacao);
    }


    // =========================================================
    // SOLICITAÇÕES
    // =========================================================

    public void carregarSolicitacoes(String cpfUsuario) {

        this.cpfUsuario = cpfUsuario;

        carregarSolicitacoes();
    }


    private void carregarSolicitacoes() {

        try {

            telaSolicitacoes.mostrarSolicitacoes(
                    emprestimoDAO.listarPendentesDoDono(cpfUsuario)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar solicitações:\n" + e.getMessage()
            );
        }
    }


    public void aceitarSolicitacao(Solicitacao solicitacao) {

        try {

            if (!emprestimoDAO.livroDisponivel(solicitacao.getIsbn())) {

                mostrarMensagem(
                        "\"" + solicitacao.getTituloLivro()
                        + "\" já está emprestado."
                );
                return;
            }

            if (emprestimoDAO.solicitanteTemEmprestimoAtivo(solicitacao.getIdSolicitacao())) {

                mostrarMensagem(
                        solicitacao.getNomeSolicitante()
                        + " já está com um livro emprestado.\n"
                        + "Cada usuário só pode pegar 1 livro por vez."
                );
                return;
            }

            if (!confirmar("Emprestar \"" + solicitacao.getTituloLivro()
                    + "\" para " + solicitacao.getNomeSolicitante() + "?")) {
                return;
            }

            emprestimoDAO.aceitar(solicitacao);

            mostrarMensagem(
                    "Solicitação aceita!\n"
                    + "Combine a entrega com " + solicitacao.getNomeSolicitante()
                    + ":\n" + solicitacao.getEmailSolicitante()
                    + "  |  " + solicitacao.getTelefoneSolicitante()
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao aceitar solicitação:\n" + e.getMessage()
            );
        }

        carregarSolicitacoes();
    }


    public void recusarSolicitacao(Solicitacao solicitacao) {

        if (!confirmar("Recusar o pedido de " + solicitacao.getNomeSolicitante()
                + " para \"" + solicitacao.getTituloLivro() + "\"?")) {
            return;
        }

        try {

            emprestimoDAO.recusar(solicitacao);

            mostrarMensagem(
                    "Solicitação recusada."
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao recusar solicitação:\n" + e.getMessage()
            );
        }

        carregarSolicitacoes();
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }


    // Pergunta Sim/Não
    private boolean confirmar(String pergunta) {

        return JanelaMensagem.confirmar(pergunta, "Confirmar");
    }
}
