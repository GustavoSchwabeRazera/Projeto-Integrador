package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.IntConsumer;
import java.util.prefs.Preferences;

import javax.swing.SwingWorker;
import javax.swing.Timer;


import View.JanelaMensagem;
import View.TelaNotificacoes;

import dao.ConnectionFactory;
import dao.EmprestimoDAO;

import model.AvisoPrazo;
import model.EmprestimoAtivo;
import model.Solicitacao;

public class NotificacaoController {

    private final EmprestimoDAO emprestimoDAO;
    private final TelaNotificacoes telaNotificacoes;

    // De quanto em quanto tempo procura notificações novas
    private static final int INTERVALO_VERIFICACAO_MS = 10_000;

    // Guarda no computador quais notificações cada usuário já viu
    private static final Preferences VISTAS =
            Preferences.userRoot().node("capasvivas/notificacoes_vistas");

    // Usuário cujas notificações estão na tela
    private String cpfUsuario;

    // Usuário logado, para o contador do sino (null = ninguém logado)
    private String cpfLogado;

    private final IntConsumer aoMudarNovas;
    private final Timer timer;
    private boolean verificando = false;
    private Connection conexaoBusca;


    public NotificacaoController(
            EmprestimoDAO emprestimoDAO,
            TelaNotificacoes telaNotificacoes,
            IntConsumer aoMudarNovas) {

        this.emprestimoDAO = emprestimoDAO;
        this.telaNotificacoes = telaNotificacoes;
        this.aoMudarNovas = aoMudarNovas;

        this.timer = new Timer(INTERVALO_VERIFICACAO_MS, e -> verificarNovas());

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

            List<AvisoPrazo> prazos = emprestimoDAO.listarPrazos(cpfUsuario);
            List<Solicitacao> pedidosRecebidos = emprestimoDAO.listarPendentesDoDono(cpfUsuario);
            List<EmprestimoAtivo> devolucoes = emprestimoDAO.listarDevolucoesParaConfirmar(cpfUsuario);

            // Abriu a tela: tudo o que está nela conta como visto
            marcarComoVistas(cpfUsuario, chavesRecebidas(prazos, pedidosRecebidos, devolucoes));

            if (cpfUsuario.equals(cpfLogado)) {
                aoMudarNovas.accept(0);
            }

            telaNotificacoes.mostrarNotificacoes(
                    // Recebidas
                    prazos,
                    pedidosRecebidos,
                    devolucoes,
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
    // CONTADOR DO SINO: notificações recebidas que o usuário ainda não viu.
    // As "Enviadas" ficam de fora, porque foi o próprio usuário que fez.
    // =========================================================

    public void iniciar(String cpfUsuario) {

        this.cpfLogado = cpfUsuario;

        verificarNovas();

        timer.start();
    }


    public void parar() {

        timer.stop();

        cpfLogado = null;

        // Com uma busca em andamento, quem fecha é o fim da busca
        if (!verificando) {
            fecharConexaoBusca();
        }

        aoMudarNovas.accept(0);
    }


    // Busca em segundo plano, com conexão própria, para a tela não travar
    private void verificarNovas() {

        if (verificando || cpfLogado == null) {
            return;
        }

        verificando = true;

        final String cpf = cpfLogado;

        new SwingWorker<Set<String>, Void>() {

            @Override
            protected Set<String> doInBackground() throws SQLException {

                if (conexaoBusca == null || conexaoBusca.isClosed()) {
                    conexaoBusca = ConnectionFactory.novaConexao();
                }

                try {

                    EmprestimoDAO dao = new EmprestimoDAO(conexaoBusca);

                    return chavesRecebidas(
                            dao.listarPrazos(cpf),
                            dao.listarPendentesDoDono(cpf),
                            dao.listarDevolucoesParaConfirmar(cpf));

                } catch (SQLException e) {

                    // Conexão caiu: a próxima busca abre outra
                    fecharConexaoBusca();

                    throw e;
                }
            }

            @Override
            protected void done() {

                verificando = false;

                // Fez logoff enquanto a busca rodava
                if (cpfLogado == null) {
                    fecharConexaoBusca();
                }

                try {

                    Set<String> atuais = get();

                    // Fez logoff ou entrou outra pessoa enquanto a busca rodava
                    if (!cpf.equals(cpfLogado)) {
                        return;
                    }

                    // Com a tela de notificações aberta, o que chega já é visto
                    if (telaNotificacoes.isVisible()) {
                        marcarComoVistas(cpf, atuais);
                        aoMudarNovas.accept(0);
                        return;
                    }

                    Set<String> vistas = lerVistas(cpf);
                    int novas = 0;

                    for (String chave : atuais) {
                        if (!vistas.contains(chave)) {
                            novas++;
                        }
                    }

                    aoMudarNovas.accept(novas);

                } catch (Exception e) {

                    // Sem mensagem na tela: ela apareceria a cada tentativa.
                    // A próxima busca tenta de novo.
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    // Um texto que identifica cada notificação recebida
    private static Set<String> chavesRecebidas(
            List<AvisoPrazo> prazos,
            List<Solicitacao> pedidosRecebidos,
            List<EmprestimoAtivo> devolucoes) {

        Set<String> chaves = new HashSet<>();

        for (AvisoPrazo a : prazos) {
            chaves.add("prazo:" + a.getIdEmprestimo());
        }

        for (Solicitacao s : pedidosRecebidos) {
            chaves.add("pedido:" + s.getIdSolicitacao());
        }

        // Com a data: se a devolução for recusada e avisada de novo, conta como nova
        for (EmprestimoAtivo d : devolucoes) {
            chaves.add("devolucao:" + d.getIdEmprestimo() + ":" + d.getDevolucaoSolicitada());
        }

        return chaves;
    }


    private static Set<String> lerVistas(String cpf) {

        String salvas = VISTAS.get(cpf, "");

        return salvas.isEmpty()
                ? new HashSet<>()
                : new HashSet<>(Arrays.asList(salvas.split(",")));
    }


    // Guarda só as notificações que ainda existem, para a lista não crescer sem fim
    private static void marcarComoVistas(String cpf, Set<String> chaves) {

        VISTAS.put(cpf, String.join(",", chaves));
    }


    private void fecharConexaoBusca() {

        try {

            if (conexaoBusca != null) {
                conexaoBusca.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();

        } finally {

            conexaoBusca = null;
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
