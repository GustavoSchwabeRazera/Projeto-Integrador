package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.ContatoChat;
import model.Mensagem;
import net.miginfocom.swing.MigLayout;

public class TelaChat extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color VERDE_FUNDO = Cores.FUNDO;
    private static final Color VERDE_ESCURO = new Color(10, 86, 27);
    private static final Color VERDE_PAINEL = new Color(36, 107, 45);
    private static final Color VERDE_BOTAO = new Color(114, 219, 145);
    private static final Color CINZA_BOLHA = new Color(232, 238, 234);
    private static final Color CINZA_TEXTO = new Color(60, 60, 60);
    private static final Color VERMELHO = new Color(190, 30, 30);

    private static final int LARGURA_MAXIMA_BOLHA = 520;

    private static final DateTimeFormatter FORMATO_HORA =
            DateTimeFormatter.ofPattern("dd/MM HH:mm");

    private final JButton btnHome;
    private final JPanel listaContatos;
    private final JPanel listaMensagens;
    private final JScrollPane scrollMensagens;
    private final JLabel lblConversa;
    private final JTextField txtMensagem;
    private final JButton btnEnviar;

    private String cpfUsuario;
    private boolean conversaVazia = true;

    private Consumer<ContatoChat> aoSelecionarContato;
    private Consumer<String> aoEnviar;


    // Painel com cantos arredondados, pintado com a cor do setForeground
    private static class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private final int radius;

        RoundedPanel(LayoutManager layout, int radius, Color cor) {
            super(layout);
            this.radius = radius;
            setOpaque(false);
            setForeground(cor);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();

            super.paintComponent(g);
        }
    }


    public TelaChat() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        JPanel contentPane = new JPanel();
        contentPane.setBackground(VERDE_FUNDO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        contentPane.setLayout(new MigLayout(
                "",
                "[200,grow][200,grow][200,grow][200,grow][200,grow]",
                "[][][grow]"));

        btnHome = new JButton("");
        btnHome.setIcon(new ImageIcon(TelaChat.class.getResource("/imagens/casa 1.png")));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

        JLabel lblLogo = new JLabel("");
        lblLogo.setIcon(new ImageIcon(TelaChat.class.getResource("/imagens/Logo.png")));
        contentPane.add(lblLogo, "cell 1 0 3 1,alignx center,aligny top");

        JLabel lblTitulo = new JLabel("Chat");
        lblTitulo.setForeground(VERDE_ESCURO);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 34));
        contentPane.add(lblTitulo, "cell 0 1 5 1,alignx center");

        RoundedPanel painel = new RoundedPanel(
                new MigLayout("insets 0", "[320!]20[grow]", "[grow]"),
                40,
                VERDE_PAINEL);
        painel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Altura pequena de propósito: quem define o tamanho é a tela,
        // e não a quantidade de mensagens
        contentPane.add(painel, "cell 0 2 5 1,grow,h 200:200:n");

        // Contatos
        listaContatos = new JPanel(new MigLayout("insets 0, wrap 1", "[grow]", "[]10[]"));
        listaContatos.setOpaque(false);
        painel.add(criarScroll(listaContatos), "cell 0 0,grow,h 100:100:n");

        // Conversa
        RoundedPanel conversa = new RoundedPanel(
                new MigLayout("insets 15 20 15 20", "[grow][]", "[]10[grow]10[]"),
                35,
                Color.WHITE);
        painel.add(conversa, "cell 1 0,grow,h 100:100:n,w 100:100:n");

        lblConversa = new JLabel(" ");
        lblConversa.setForeground(VERDE_ESCURO);
        lblConversa.setFont(new Font("Tahoma", Font.BOLD, 22));
        conversa.add(lblConversa, "cell 0 0 2 1");

        listaMensagens = new JPanel(new MigLayout("insets 5, wrap 1", "[grow]", "[]8[]"));
        listaMensagens.setOpaque(false);

        scrollMensagens = criarScroll(listaMensagens);
        conversa.add(scrollMensagens, "cell 0 1 2 1,grow,h 100:100:n");

        txtMensagem = new JTextField();
        txtMensagem.setFont(new Font("Tahoma", Font.PLAIN, 16));
        txtMensagem.setPreferredSize(new Dimension(300, 40));
        txtMensagem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_ESCURO, 2),
                new EmptyBorder(0, 10, 0, 10)));
        txtMensagem.addActionListener(e -> enviarTexto());
        conversa.add(txtMensagem, "cell 0 2,growx");

        btnEnviar = new TelaMeusLivros.BotaoArredondado("Enviar", 40);
        btnEnviar.setBackground(VERDE_BOTAO);
        btnEnviar.setForeground(Color.BLACK);
        btnEnviar.setFont(new Font("Tahoma", Font.BOLD, 15));
        btnEnviar.addActionListener(e -> enviarTexto());
        conversa.add(btnEnviar, "cell 1 2,h 40!,w 110!");

        mostrarSemConversa("Selecione um contato para conversar.");
    }


    private JScrollPane criarScroll(JPanel conteudo) {

        JScrollPane scroll = new JScrollPane(conteudo);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        return scroll;
    }


    private void enviarTexto() {

        if (aoEnviar != null && txtMensagem.isEnabled()) {
            aoEnviar.accept(txtMensagem.getText());
        }
    }


    // =====================================================
    // CONTATOS
    // =====================================================

    public void mostrarContatos(List<ContatoChat> contatos, String cpfSelecionado) {

        listaContatos.removeAll();

        if (contatos.isEmpty()) {

            JTextArea aviso = new JTextArea(
                    "Nenhum contato ainda.\n\nO chat aparece quando você tem uma solicitação em comum com alguém.");
            aviso.setForeground(Color.WHITE);
            aviso.setFont(new Font("Tahoma", Font.BOLD, 16));
            aviso.setOpaque(false);
            aviso.setEditable(false);
            aviso.setFocusable(false);
            aviso.setLineWrap(true);
            aviso.setWrapStyleWord(true);
            listaContatos.add(aviso, "growx,w 280!,gaptop 10");

        } else {

            for (ContatoChat contato : contatos) {
                listaContatos.add(
                        criarCartaoContato(contato, contato.getCpf().equals(cpfSelecionado)),
                        "growx");
            }
        }

        listaContatos.revalidate();
        listaContatos.repaint();
    }


    private JPanel criarCartaoContato(ContatoChat contato, boolean selecionado) {

        RoundedPanel cartao = new RoundedPanel(
                new MigLayout("insets 10 15 10 15", "[]12[grow]8[]", "[center]"),
                30,
                selecionado ? VERDE_BOTAO : Color.WHITE);
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cartao.add(new JLabel(new ImageIcon(pegarFoto(contato.getFoto(), 45))), "cell 0 0");

        JLabel lblNome = new JLabel(contato.getNome());
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 18));
        cartao.add(lblNome, "cell 1 0,growx,w 0:0:n");

        if (contato.getNaoLidas() > 0) {

            RoundedPanel contador = new RoundedPanel(
                    new MigLayout("insets 2 8 2 8", "[]", "[]"), 20, VERMELHO);

            JLabel lblContador = new JLabel(String.valueOf(contato.getNaoLidas()));
            lblContador.setForeground(Color.WHITE);
            lblContador.setFont(new Font("Tahoma", Font.BOLD, 14));
            contador.add(lblContador);

            cartao.add(contador, "cell 2 0");
        }

        cartao.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (aoSelecionarContato != null) {
                    aoSelecionarContato.accept(contato);
                }
            }
        });

        return cartao;
    }


    // Foto do usuário (ou a imagem padrão se ele não tiver)
    private Image pegarFoto(byte[] bytes, int tamanho) {

        Image imagem = null;

        if (bytes != null && bytes.length > 0) {
            try {
                imagem = ImageIO.read(new ByteArrayInputStream(bytes));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (imagem == null) {
            imagem = new ImageIcon(TelaChat.class.getResource("/imagens/perfil3.png")).getImage();
        }

        return imagem.getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH);
    }


    // =====================================================
    // CONVERSA
    // =====================================================

    // Sem conversa aberta: mostra o aviso e trava o campo de texto
    public void mostrarSemConversa(String aviso) {

        lblConversa.setText(" ");

        limparMensagens(aviso);

        txtMensagem.setText("");
        txtMensagem.setEnabled(false);
        btnEnviar.setEnabled(false);
    }


    public void mostrarConversa(String nomeContato, List<Mensagem> mensagens) {

        lblConversa.setText(nomeContato);

        limparMensagens("Nenhuma mensagem ainda. Mande a primeira!");

        txtMensagem.setText("");
        txtMensagem.setEnabled(true);
        btnEnviar.setEnabled(true);
        txtMensagem.requestFocusInWindow();

        adicionarMensagens(mensagens, true);
    }


    private void limparMensagens(String aviso) {

        listaMensagens.removeAll();
        conversaVazia = true;

        JLabel lblAviso = new JLabel(aviso);
        lblAviso.setForeground(CINZA_TEXTO);
        lblAviso.setFont(new Font("Tahoma", Font.PLAIN, 17));
        listaMensagens.add(lblAviso, "alignx center,gaptop 40");

        listaMensagens.revalidate();
        listaMensagens.repaint();
    }


    // Quem está lendo mensagens antigas não é puxado para o fim,
    // a não ser que a mensagem seja a sua
    public void adicionarMensagens(List<Mensagem> mensagens, boolean irParaOFim) {

        if (mensagens.isEmpty()) {
            return;
        }

        JScrollBar barra = scrollMensagens.getVerticalScrollBar();

        boolean estavaNoFim =
                barra.getValue() + barra.getVisibleAmount() >= barra.getMaximum() - 40;

        if (conversaVazia) {
            listaMensagens.removeAll();
            conversaVazia = false;
        }

        for (Mensagem mensagem : mensagens) {

            boolean minha = mensagem.getCpfRemetente().equals(cpfUsuario);

            listaMensagens.add(criarBolha(mensagem, minha), minha ? "alignx right" : "alignx left");
        }

        listaMensagens.revalidate();
        listaMensagens.repaint();

        if (irParaOFim || estavaNoFim) {
            SwingUtilities.invokeLater(() -> {
                scrollMensagens.validate();
                barra.setValue(barra.getMaximum());
            });
        }
    }


    private JPanel criarBolha(Mensagem mensagem, boolean minha) {

        RoundedPanel bolha = new RoundedPanel(
                new MigLayout("insets 8 14 8 14, wrap 1", "[]", "[]2[]"),
                25,
                minha ? VERDE_BOTAO : CINZA_BOLHA);

        JTextArea texto = new JTextArea(mensagem.getTexto());
        texto.setFont(new Font("Tahoma", Font.PLAIN, 16));
        texto.setForeground(new Color(30, 30, 30));
        texto.setOpaque(false);
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);

        // Mensagem curta fica em uma bolha estreita; a altura é calculada
        // depois de fixar a largura, para contar as linhas quebradas
        int largura = Math.min(
                LARGURA_MAXIMA_BOLHA,
                texto.getFontMetrics(texto.getFont()).stringWidth(mensagem.getTexto()) + 4);

        texto.setSize(new Dimension(largura, Short.MAX_VALUE));
        texto.setPreferredSize(new Dimension(largura, texto.getPreferredSize().height));

        bolha.add(texto);

        JLabel lblHora = new JLabel(mensagem.getDataHora().format(FORMATO_HORA));
        lblHora.setForeground(CINZA_TEXTO);
        lblHora.setFont(new Font("Tahoma", Font.PLAIN, 12));
        bolha.add(lblHora, "alignx right");

        return bolha;
    }


    // =====================================================
    // CAMPO DE TEXTO
    // =====================================================

    public void limparCampoMensagem() {

        txtMensagem.setText("");
        txtMensagem.requestFocusInWindow();
    }


    // =====================================================
    // GETTERS E SETTERS
    // =====================================================

    public JButton getBtnHome() {
        return btnHome;
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public void setAoSelecionarContato(Consumer<ContatoChat> aoSelecionarContato) {
        this.aoSelecionarContato = aoSelecionarContato;
    }

    public void setAoEnviar(Consumer<String> aoEnviar) {
        this.aoEnviar = aoEnviar;
    }
}
