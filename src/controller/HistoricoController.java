package controller;

import java.sql.SQLException;


import View.JanelaMensagem;
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

        JanelaMensagem.mostrar(mensagem);
    }
}
