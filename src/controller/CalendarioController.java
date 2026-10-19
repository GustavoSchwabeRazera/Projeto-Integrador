package controller;

import java.awt.Color;
import java.sql.SQLException;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import View.Calendario;

import dao.EmprestimoDAO;
import dao.EventoDAO;

import model.Evento;

public class CalendarioController {

    private final EventoDAO eventoDAO;
    private final EmprestimoDAO emprestimoDAO;
    private final Calendario calendario;

    // Usuário cujo calendário está na tela
    private String cpfUsuario;


    public CalendarioController(
            EventoDAO eventoDAO,
            EmprestimoDAO emprestimoDAO,
            Calendario calendario) {

        this.eventoDAO = eventoDAO;
        this.emprestimoDAO = emprestimoDAO;
        this.calendario = calendario;
    }


    // =========================================================
    // CARREGAR
    // =========================================================

    public void carregarCalendario(String cpfUsuario) {

        this.cpfUsuario = cpfUsuario;

        carregarCalendario();
    }


    private void carregarCalendario() {

        try {

            calendario.mostrarEventos(
                    emprestimoDAO.listarEventosCalendario(cpfUsuario),
                    eventoDAO.listarPorUsuario(cpfUsuario)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar o calendário:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // ADICIONAR LEMBRETE
    // =========================================================

    public void adicionarEvento() {

        String descricao =
                calendario.getCampoNovoEvento()
                        .getText()
                        .trim();

        if (descricao.isEmpty()) {

            mostrarMensagem(
                    "Digite o lembrete antes de adicionar."
            );

            return;
        }

        if (descricao.length() > 100) {

            mostrarMensagem(
                    "O lembrete deve ter no máximo 100 caracteres."
            );

            return;
        }

        try {

            eventoDAO.adicionar(
                    cpfUsuario,
                    calendario.getDataSelecionada(),
                    descricao
            );

            calendario.getCampoNovoEvento().setText("");

            carregarCalendario();

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao salvar o lembrete:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // REMOVER LEMBRETE
    // =========================================================

    public void removerEvento() {

        Evento evento = calendario.getEventoSelecionado();

        if (evento == null) {

            mostrarMensagem(
                    "Selecione um lembrete na lista para remover."
            );

            return;
        }

        try {

            eventoDAO.remover(evento.getIdEvento());

            carregarCalendario();

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao remover o lembrete:\n" + e.getMessage()
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
