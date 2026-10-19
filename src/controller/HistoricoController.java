package controller;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Historico;

import dao.HistoricoDAO;

public class HistoricoController {

    private final HistoricoDAO historicoDAO;
    private final Historico historico;


    public HistoricoController(
            HistoricoDAO historicoDAO,
            Historico historico) {

        this.historicoDAO = historicoDAO;
        this.historico = historico;
    }


    // =========================================================
    // HISTÓRICO
    // =========================================================

    public void carregarHistorico(String cpfUsuario) {

        try {

            historico.mostrarHistorico(
                    historicoDAO.listarPorUsuario(cpfUsuario)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar o histórico:\n" + e.getMessage()
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
