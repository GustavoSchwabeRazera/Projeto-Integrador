package controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

import javax.swing.SwingWorker;
import javax.swing.Timer;

import View.JanelaMensagem;
import View.TelaChat;

import dao.ConnectionFactory;
import dao.MensagemDAO;

import model.ContatoChat;
import model.Mensagem;

// De tempos em tempos procura mensagens novas no banco. A busca roda fora da
// thread da interface e em uma conexão só dela, porque a conexão compartilhada
// do sistema também é usada nas transações dos empréstimos.
public class ChatController {

    private static final int INTERVALO_ATUALIZACAO_MS = 2500;
    private static final int TAMANHO_MAXIMO_MENSAGEM = 500;

    private final MensagemDAO mensagemDAO;
    private final TelaChat telaChat;
    private final IntConsumer aoMudarNaoLidas;
    private final Timer timer;

    private String cpfUsuario;
    private ContatoChat contatoAberto;
    private int ultimoIdMostrado;

    // Evita desenhar a lista de novo quando nada mudou
    private String assinaturaContatos = "";

    // Só uma busca por vez
    private boolean atualizando = false;

    // Conexão da busca automática, reaproveitada entre uma busca e outra
    private volatile Connection conexaoBusca;

    // Fotos já baixadas nesta sessão; quem não tem foto fica com o vetor vazio
    private final Map<String, byte[]> fotos = new ConcurrentHashMap<>();


    public ChatController(
            MensagemDAO mensagemDAO,
            TelaChat telaChat,
            IntConsumer aoMudarNaoLidas) {

        this.mensagemDAO = mensagemDAO;
        this.telaChat = telaChat;
        this.aoMudarNaoLidas = aoMudarNaoLidas;

        this.timer = new Timer(INTERVALO_ATUALIZACAO_MS, e -> atualizar());

        telaChat.setAoSelecionarContato(this::abrirConversa);
        telaChat.setAoEnviar(this::enviar);
    }


    // =========================================================
    // LOGIN E LOGOFF
    // =========================================================

    public void iniciar(String cpfUsuario) {

        this.cpfUsuario = cpfUsuario;

        telaChat.setCpfUsuario(cpfUsuario);

        atualizar();

        timer.start();
    }


    public void parar() {

        timer.stop();

        cpfUsuario = null;
        contatoAberto = null;
        assinaturaContatos = "";
        fotos.clear();

        // Com uma busca em andamento, quem fecha é o fim da busca
        if (!atualizando) {
            fecharConexaoBusca();
        }

        aoMudarNaoLidas.accept(0);
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
    // ABRIR O CHAT
    // =========================================================

    public void carregarChat() {

        contatoAberto = null;

        telaChat.mostrarSemConversa("Selecione um contato para conversar.");

        recarregarContatos();
    }


    // Vem direto de uma solicitação: já abre a conversa com a pessoa
    public void carregarChatCom(String cpfContato) {

        carregarChat();

        try {

            for (ContatoChat contato : mensagemDAO.listarContatos(cpfUsuario)) {

                if (contato.getCpf().equals(cpfContato)) {
                    abrirConversa(contato);
                    return;
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao abrir a conversa:\n" + e.getMessage());
        }
    }


    private void recarregarContatos() {

        try {

            mostrarContatos(listarContatosComFoto(mensagemDAO, cpfUsuario));

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao carregar os contatos:\n" + e.getMessage());
        }
    }


    // Cada foto só é baixada uma vez por sessão
    private List<ContatoChat> listarContatosComFoto(MensagemDAO dao, String cpf) throws SQLException {

        List<ContatoChat> contatos = dao.listarContatos(cpf);

        for (ContatoChat contato : contatos) {

            byte[] foto = fotos.get(contato.getCpf());

            if (foto == null) {

                foto = dao.buscarFoto(contato.getCpf());

                if (foto == null) {
                    foto = new byte[0];
                }

                fotos.put(contato.getCpf(), foto);
            }

            contato.setFoto(foto);
        }

        return contatos;
    }


    private void mostrarContatos(List<ContatoChat> contatos) {

        String cpfAberto = contatoAberto != null ? contatoAberto.getCpf() : "";

        String assinatura = cpfAberto + "|" + contatos.stream()
                .map(c -> c.getCpf() + ":" + c.getNome() + ":" + c.getNaoLidas())
                .collect(Collectors.joining(";"));

        if (!assinatura.equals(assinaturaContatos)) {

            assinaturaContatos = assinatura;

            telaChat.mostrarContatos(contatos, cpfAberto);
        }
    }


    private void abrirConversa(ContatoChat contato) {

        try {

            mensagemDAO.marcarComoLidas(contato.getCpf(), cpfUsuario);

            List<Mensagem> mensagens =
                    mensagemDAO.listarConversa(cpfUsuario, contato.getCpf());

            contatoAberto = contato;

            ultimoIdMostrado = ultimoId(mensagens, 0);

            telaChat.mostrarConversa(contato.getNome(), mensagens);

            recarregarContatos();

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao abrir a conversa:\n" + e.getMessage());
        }
    }


    // =========================================================
    // ENVIAR MENSAGEM
    // =========================================================

    private void enviar(String textoDigitado) {

        String texto = textoDigitado.trim();

        if (texto.isEmpty() || contatoAberto == null) {
            return;
        }

        if (texto.length() > TAMANHO_MAXIMO_MENSAGEM) {

            mostrarMensagem(
                    "A mensagem deve ter no máximo " + TAMANHO_MAXIMO_MENSAGEM + " caracteres."
            );

            return;
        }

        try {

            mensagemDAO.enviar(cpfUsuario, contatoAberto.getCpf(), texto);

            telaChat.limparCampoMensagem();

            mostrarNovas(
                    mensagemDAO.listarNovas(cpfUsuario, contatoAberto.getCpf(), ultimoIdMostrado),
                    true
            );

        } catch (SQLException e) {

            e.printStackTrace();

            mostrarMensagem("Erro ao enviar a mensagem:\n" + e.getMessage());
        }
    }


    // A busca automática e o envio podem trazer a mesma mensagem
    private void mostrarNovas(List<Mensagem> mensagens, boolean irParaOFim) {

        List<Mensagem> novas = new ArrayList<>();

        for (Mensagem mensagem : mensagens) {

            if (mensagem.getIdMensagem() > ultimoIdMostrado) {
                novas.add(mensagem);
            }
        }

        ultimoIdMostrado = ultimoId(novas, ultimoIdMostrado);

        telaChat.adicionarMensagens(novas, irParaOFim);
    }


    private int ultimoId(List<Mensagem> mensagens, int padrao) {

        return mensagens.isEmpty()
                ? padrao
                : mensagens.get(mensagens.size() - 1).getIdMensagem();
    }


    // =========================================================
    // ATUALIZAÇÃO AUTOMÁTICA
    // =========================================================

    private static class Resultado {
        int naoLidas;
        List<ContatoChat> contatos;
        List<Mensagem> novas = new ArrayList<>();
    }


    private void atualizar() {

        if (atualizando || cpfUsuario == null) {
            return;
        }

        atualizando = true;

        final String cpf = cpfUsuario;
        final boolean chatVisivel = telaChat.isVisible();
        final String cpfAberto =
                chatVisivel && contatoAberto != null ? contatoAberto.getCpf() : null;
        final int ultimoId = ultimoIdMostrado;

        new SwingWorker<Resultado, Void>() {

            @Override
            protected Resultado doInBackground() throws SQLException {

                if (conexaoBusca == null || conexaoBusca.isClosed()) {
                    conexaoBusca = ConnectionFactory.novaConexao();
                }

                try {

                    MensagemDAO dao = new MensagemDAO(conexaoBusca);
                    Resultado resultado = new Resultado();

                    if (cpfAberto != null) {

                        resultado.novas = dao.listarNovas(cpf, cpfAberto, ultimoId);

                        // Antes de contar, para a conversa aberta não aparecer como não lida
                        if (!resultado.novas.isEmpty()) {
                            dao.marcarComoLidas(cpfAberto, cpf);
                        }
                    }

                    resultado.naoLidas = dao.contarNaoLidas(cpf);

                    if (chatVisivel) {
                        resultado.contatos = listarContatosComFoto(dao, cpf);
                    }

                    return resultado;

                } catch (SQLException e) {

                    // Conexão caiu: a próxima busca abre outra
                    fecharConexaoBusca();

                    throw e;
                }
            }

            @Override
            protected void done() {

                atualizando = false;

                // Fez logoff enquanto a busca rodava
                if (cpfUsuario == null) {
                    fecharConexaoBusca();
                }

                try {

                    // Fez logoff ou entrou outra pessoa enquanto a busca rodava
                    if (cpf.equals(cpfUsuario)) {
                        aplicar(get(), cpfAberto);
                    }

                } catch (Exception e) {

                    // Sem mensagem na tela: ela apareceria a cada tentativa.
                    // A próxima busca tenta de novo.
                    e.printStackTrace();
                }
            }
        }.execute();
    }


    private void aplicar(Resultado resultado, String cpfAberto) {

        aoMudarNaoLidas.accept(resultado.naoLidas);

        if (cpfAberto != null
                && contatoAberto != null
                && cpfAberto.equals(contatoAberto.getCpf())) {

            mostrarNovas(resultado.novas, false);
        }

        if (resultado.contatos == null) {
            return;
        }

        if (contatoAberto != null && !contem(resultado.contatos, contatoAberto.getCpf())) {

            contatoAberto = null;

            telaChat.mostrarSemConversa("Esta conversa foi encerrada: a solicitação não existe mais.");
        }

        mostrarContatos(resultado.contatos);
    }


    private boolean contem(List<ContatoChat> contatos, String cpf) {

        return contatos.stream().anyMatch(c -> c.getCpf().equals(cpf));
    }


    // =========================================================
    // MENSAGEM
    // =========================================================

    private void mostrarMensagem(String mensagem) {

        JanelaMensagem.mostrar(mensagem);
    }
}
