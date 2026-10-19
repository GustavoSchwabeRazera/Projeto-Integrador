package controller;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.TelaNotificacoes;

import dao.EmprestimoDAO;

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
