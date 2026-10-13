package controller;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

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


    private void aceitarSolicitacao(Solicitacao solicitacao) {

        try {

            if (!emprestimoDAO.livroDisponivel(solicitacao.getIsbn())) {

                mostrarMensagem(
                        "\"" + solicitacao.getTituloLivro()
                        + "\" já está emprestado."
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


    private void recusarSolicitacao(Solicitacao solicitacao) {

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


    // Pergunta Sim/Não com a janela verde
    private boolean confirmar(String pergunta) {

        UIManager.put(
                "OptionPane.background",
                new Color(175, 244, 198)
        );

        UIManager.put(
                "Panel.background",
                new Color(175, 244, 198)
        );

        Object[] opcoes = { "Sim", "Não" };

        int resposta = JOptionPane.showOptionDialog(
                null,
                pergunta,
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                opcoes[1]
        );

        UIManager.put(
                "OptionPane.background",
                null
        );

        UIManager.put(
                "Panel.background",
                null
        );

        return resposta == 0;
    }
}
