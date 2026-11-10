package controller;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


import View.JanelaMensagem;
import View.TelaSolicitacoes;

import dao.EmprestimoDAO;

import model.Solicitacao;

public class SolicitacaoController {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final EmprestimoDAO emprestimoDAO;
    private final TelaSolicitacoes telaSolicitacoes;

    // Usuário cujas solicitações (recebidas e enviadas) estão na tela
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
                    // Recebidas: pedidos para os livros do usuário
                    emprestimoDAO.listarPendentesDoDono(cpfUsuario),
                    // Enviadas: pedidos que o usuário fez
                    emprestimoDAO.listarPedidosEnviados(cpfUsuario)
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

            LocalDate hoje = LocalDate.now();
            LocalDate retirada = solicitacao.getDataRetirada();
            LocalDate retorno = solicitacao.getDataRetorno();
            String aviso = "";

            if (retirada == null || retorno == null) {
                // Pedido antigo, feito antes de escolher as datas
                retirada = hoje;
                retorno = hoje.plusDays(EmprestimoDAO.DIAS_EMPRESTIMO);

            } else if (retirada.isBefore(hoje)) {
                // O dia de retirada já passou: começa hoje e mantém a mesma duração
                long dias = ChronoUnit.DAYS.between(retirada, retorno);
                retirada = hoje;
                retorno = hoje.plusDays(dias);
                aviso = "\n(O dia de retirada escolhido já passou, então as datas foram adiadas.)";
            }

            if (!confirmar("Emprestar \"" + solicitacao.getTituloLivro()
                    + "\" para " + solicitacao.getNomeSolicitante() + "?\n"
                    + "Retirada: " + retirada.format(FORMATO_DATA) + "\n"
                    + "Devolução: " + retorno.format(FORMATO_DATA)
                    + aviso)) {
                return;
            }

            emprestimoDAO.aceitar(solicitacao, retirada, retorno);

            mostrarMensagem(
                    "Solicitação aceita!\n"
                    + "Entregue o livro em " + retirada.format(FORMATO_DATA)
                    + " e receba de volta em " + retorno.format(FORMATO_DATA) + ".\n"
                    + "As datas já estão no seu calendário.\n"
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
