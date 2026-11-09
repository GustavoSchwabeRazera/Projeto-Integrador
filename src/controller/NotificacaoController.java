package controller;

import java.sql.SQLException;
import java.util.List;


import View.JanelaMensagem;
import View.TelaNotificacoes;

import dao.EmprestimoDAO;

import model.AvisoPrazo;
import model.EmprestimoAtivo;

public class NotificacaoController {

    private final EmprestimoDAO emprestimoDAO;
    private final TelaNotificacoes telaNotificacoes;

    // Usuário cujas notificações estão na tela
    private String cpfUsuario;


    public NotificacaoController(
            EmprestimoDAO emprestimoDAO,
            TelaNotificacoes telaNotificacoes) {

        this.emprestimoDAO = emprestimoDAO;
        this.telaNotificacoes = telaNotificacoes;

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
                    // Recebidas
                    emprestimoDAO.listarPrazos(cpfUsuario),
                    emprestimoDAO.listarPendentesDoDono(cpfUsuario),
                    emprestimoDAO.listarDevolucoesParaConfirmar(cpfUsuario),
                    // Enviadas
                    emprestimoDAO.listarPedidosEnviados(cpfUsuario),
                    emprestimoDAO.listarEmprestimosAceitos(cpfUsuario),
                    emprestimoDAO.listarDevolucoesInformadas(cpfUsuario)
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao carregar notificações:\n" + e.getMessage()
            );
        }
    }


    // =========================================================
    // AVISO DIÁRIO DO PRAZO DE DEVOLUÇÃO
    // Aparece uma vez por dia, enquanto o livro não for devolvido
    // =========================================================

    public void avisarPrazosDoDia(String cpfUsuario) {

        try {

            List<AvisoPrazo> avisos = emprestimoDAO.listarPrazosNaoAvisadosHoje(cpfUsuario);

            if (avisos.isEmpty()) {
                return;
            }

            StringBuilder texto = new StringBuilder("Lembrete de devolução:");

            for (AvisoPrazo a : avisos) {
                texto.append("\n\n").append(a.getLembrete());
            }

            texto.append("\n\nConsulte o prazo em Livros Comigo.");

            mostrarMensagem(texto.toString());

            emprestimoDAO.marcarPrazosAvisadosHoje(cpfUsuario);

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem(
                    "Erro ao verificar os prazos de devolução:\n" + e.getMessage()
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
