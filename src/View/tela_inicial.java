package View;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class tela_inicial extends JFrame {

    private static final long serialVersionUID = 1L;

    // Tamanho dos ícones da barra de cima e da foto de perfil
    private static final int TAMANHO_ICONE = 44;
    private static final int TAMANHO_FOTO = 60;

    private static final Color VERDE_ESCURO = new Color(10, 86, 27);
    private static final Color VERDE_TEXTO = new Color(40, 90, 50);
    private static final String FONTE = "Segoe UI";

    private JPanel contentPane;

    private JButton btnPesquisar;
    private JButton btnMeusLivros;
    private JButton btnSolicitacoes;
    private JButton btnLivrosComigo;
    private JButton btnPerfil;
    private JButton btnCalendario;
    private JButton btnHistorico;
    private JButton btnNotificacao;
    private JButton btnLogo;
    private JButton btnChat;
    private JButton btnSair;
    private int naoLidasChat = 0;
    private int notificacoesNovas = 0;
    private Image imagemSino;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    tela_inicial frame = new tela_inicial();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public tela_inicial() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel(new BorderLayout());
        contentPane.setBackground(Cores.FUNDO);
        contentPane.setBorder(new EmptyBorder(16, 28, 20, 28));
        setContentPane(contentPane);

        contentPane.add(criarBarraSuperior(), BorderLayout.NORTH);
        contentPane.add(criarCentro(), BorderLayout.CENTER);
        contentPane.add(criarRodape(), BorderLayout.SOUTH);
    }

    // =========================================================
    // BARRA DE CIMA: logo à esquerda, atalhos e perfil à direita
    // =========================================================
    private JPanel criarBarraSuperior() {

        JPanel barra = new JPanel(new BorderLayout());
        barra.setOpaque(false);

        btnLogo = botaoIcone(new ImageIcon(tela_inicial.class.getResource("/imagens/LogoPequena.png")), "Sobre o Capas Vivas");
        barra.add(btnLogo, BorderLayout.WEST);

        JPanel atalhos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        atalhos.setOpaque(false);

        btnCalendario = botaoIcone(icone("/imagens/calendario.png", TAMANHO_ICONE), "Calendário");
        // Sem ActionListener aqui: o LivroController é quem registra as ações.
        btnChat = botaoIcone(IconeChat.criar(TAMANHO_ICONE, 0), "Chat");
        imagemSino = new ImageIcon(tela_inicial.class.getResource("/imagens/notificacao.png")).getImage();
        btnNotificacao = botaoIcone(IconeChat.comContador(imagemSino, TAMANHO_ICONE, 0), "Notificações");
        btnPerfil = botaoIcone(icone("/imagens/perfil3.png", TAMANHO_FOTO), "Perfil");

        atalhos.add(btnCalendario);
        atalhos.add(btnChat);
        atalhos.add(btnNotificacao);
        atalhos.add(separador());
        atalhos.add(btnPerfil);

        // Centraliza os ícones na altura do logo
        JPanel alinhador = new JPanel(new GridBagLayout());
        alinhador.setOpaque(false);
        alinhador.add(atalhos);
        barra.add(alinhador, BorderLayout.EAST);

        return barra;
    }

    // =========================================================
    // CENTRO: boas-vindas e os quatro cartões
    // =========================================================
    private JPanel criarCentro() {

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setOpaque(false);

        JLabel lblBemVindo = new JLabel("Bem-vindo(a)!", SwingConstants.CENTER);
        lblBemVindo.setForeground(VERDE_ESCURO);
        lblBemVindo.setFont(new Font(FONTE, Font.BOLD, 46));

        JLabel lblSubtitulo = new JLabel("O que você quer fazer hoje?", SwingConstants.CENTER);
        lblSubtitulo.setForeground(VERDE_TEXTO);
        lblSubtitulo.setFont(new Font(FONTE, Font.PLAIN, 20));

        btnPesquisar = new CartaoMenu("Pesquisar", "Encontre livros para pegar emprestado");
        btnMeusLivros = new CartaoMenu("Meus Livros", "Cadastre e gerencie os seus livros");
        btnSolicitacoes = new CartaoMenu("Solicitações", "Acompanhe os pedidos de empréstimo");
        btnLivrosComigo = new CartaoMenu("Livros Comigo", "Livros que você pegou emprestado");

        JPanel grade = new JPanel(new GridLayout(2, 2, 28, 28));
        grade.setOpaque(false);
        grade.add(btnPesquisar);
        grade.add(btnMeusLivros);
        grade.add(btnSolicitacoes);
        grade.add(btnLivrosComigo);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        centro.add(lblBemVindo, c);

        c.gridy = 1;
        c.insets = new Insets(4, 0, 40, 0);
        centro.add(lblSubtitulo, c);

        c.gridy = 2;
        c.insets = new Insets(0, 0, 40, 0);
        centro.add(grade, c);

        return centro;
    }

    // =========================================================
    // RODAPÉ: histórico à esquerda, sair à direita
    // =========================================================
    private JPanel criarRodape() {

        JPanel rodape = new JPanel(new BorderLayout());
        rodape.setOpaque(false);

        btnHistorico = botaoIcone(new IconeLista(26, VERDE_ESCURO), null);
        btnHistorico.setText("Histórico");
        btnHistorico.setIconTextGap(12);
        btnHistorico.setFont(new Font(FONTE, Font.BOLD, 20));
        btnHistorico.setForeground(VERDE_ESCURO);
        // O dispose() que estava aqui foi REMOVIDO: quem controla a navegação é o Controller.
        rodape.add(btnHistorico, BorderLayout.WEST);

        btnSair = botaoIcone(new IconeSair(26, VERDE_ESCURO), "Sair da conta");
        btnSair.setText("Sair");
        btnSair.setIconTextGap(10);
        btnSair.setFont(new Font(FONTE, Font.BOLD, 20));
        btnSair.setForeground(VERDE_ESCURO);
        rodape.add(btnSair, BorderLayout.EAST);

        return rodape;
    }

    // =========================================================
    // AJUDANTES
    // =========================================================

    // Botão só com ícone, sem fundo nem borda
    private JButton botaoIcone(Icon icone, String dica) {

        JButton botao = new JButton(icone);
        botao.setContentAreaFilled(false);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
        botao.setMargin(new Insets(0, 0, 0, 0));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setToolTipText(dica);
        return botao;
    }

    private ImageIcon icone(String caminho, int tamanho) {

        Image imagem = new ImageIcon(tela_inicial.class.getResource(caminho)).getImage();
        return new ImageIcon(imagem.getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH));
    }

    // Risquinho vertical entre os atalhos e a foto de perfil
    private JPanel separador() {

        JPanel linha = new JPanel();
        linha.setBackground(new Color(10, 86, 27, 70));
        linha.setPreferredSize(new Dimension(2, 40));
        return linha;
    }

    public void atualizarFotoPerfil(byte[] foto) {

        Image imagem;

        // Se não tiver foto cadastrada
        if (foto == null || foto.length == 0) {

            ImageIcon fotoPadrao = new ImageIcon(
                tela_inicial.class.getResource("/imagens/perfil3.png")
            );

            imagem = fotoPadrao.getImage();

        } else {

            // Foto cadastrada no banco
            imagem = new ImageIcon(foto).getImage();
        }

        int tamanho = TAMANHO_FOTO;

        BufferedImage circular = new BufferedImage(
            tamanho,
            tamanho,
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g2 = circular.createGraphics();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        g2.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BICUBIC
        );

        g2.setClip(new Ellipse2D.Double(
            0, 0, tamanho, tamanho
        ));

        int largura = imagem.getWidth(null);
        int altura = imagem.getHeight(null);

        double escala = Math.max(
            (double) tamanho / largura,
            (double) tamanho / altura
        );

        int novaLargura = (int) (largura * escala);
        int novaAltura = (int) (altura * escala);

        int x = (tamanho - novaLargura) / 2;
        int y = (tamanho - novaAltura) / 2;

        g2.drawImage(
            imagem,
            x,
            y,
            novaLargura,
            novaAltura,
            null
        );

        g2.dispose();

        btnPerfil.setIcon(new ImageIcon(circular));

        btnPerfil.revalidate();
        btnPerfil.repaint();
    }

    // Mostra no balão do chat quantas mensagens estão sem ler
    public void atualizarNaoLidasChat(int naoLidas) {

        if (naoLidas != naoLidasChat) {
            naoLidasChat = naoLidas;
            btnChat.setIcon(IconeChat.criar(TAMANHO_ICONE, naoLidas));
        }
    }

    // Mostra no sino quantas notificações novas ainda não foram vistas
    public void atualizarNotificacoesNovas(int novas) {

        if (novas != notificacoesNovas) {
            notificacoesNovas = novas;
            btnNotificacao.setIcon(IconeChat.comContador(imagemSino, TAMANHO_ICONE, novas));
        }
    }

    // =========================================================
    // CARTÃO DO MENU: verde arredondado, com título e descrição.
    // Fica mais escuro quando o mouse passa por cima.
    // =========================================================
    private static class CartaoMenu extends JButton {

        private static final long serialVersionUID = 1L;

        private static final Color COR = Cores.VERDE;
        private static final Color COR_HOVER = Cores.VERDE_HOVER;
        private static final Color SOMBRA = new Color(0, 60, 20, 45);
        private static final Color DESCRICAO = new Color(225, 245, 230);
        private static final int ARCO = 28;
        private static final int ALTURA_SOMBRA = 5;

        private final String titulo;
        private final String descricao;

        CartaoMenu(String titulo, String descricao) {

            this.titulo = titulo;
            this.descricao = descricao;

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(380, 150));
            getAccessibleContext().setAccessibleName(titulo);
        }

        @Override
        protected void paintComponent(Graphics g) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int largura = getWidth();
            int altura = getHeight() - ALTURA_SOMBRA;
            boolean emCima = getModel().isRollover() || getModel().isPressed();
            int deslocamento = getModel().isPressed() ? 2 : 0;

            // Sombra suave embaixo
            g2.setColor(SOMBRA);
            g2.fillRoundRect(0, ALTURA_SOMBRA, largura, altura, ARCO, ARCO);

            // Cartão
            g2.setColor(emCima ? COR_HOVER : COR);
            g2.fillRoundRect(0, deslocamento, largura, altura, ARCO, ARCO);

            // Título e descrição centralizados
            Font fonteTitulo = new Font(FONTE, Font.BOLD, 28);
            Font fonteDescricao = new Font(FONTE, Font.PLAIN, 16);
            FontMetrics mTitulo = g2.getFontMetrics(fonteTitulo);
            FontMetrics mDescricao = g2.getFontMetrics(fonteDescricao);

            int alturaTexto = mTitulo.getHeight() + 4 + mDescricao.getHeight();
            int topo = (altura - alturaTexto) / 2 + deslocamento;

            g2.setFont(fonteTitulo);
            g2.setColor(Color.WHITE);
            g2.drawString(titulo,
                    (largura - mTitulo.stringWidth(titulo)) / 2,
                    topo + mTitulo.getAscent());

            g2.setFont(fonteDescricao);
            g2.setColor(DESCRICAO);
            g2.drawString(descricao,
                    (largura - mDescricao.stringWidth(descricao)) / 2,
                    topo + mTitulo.getHeight() + 4 + mDescricao.getAscent());

            g2.dispose();
        }
    }

    // Ícone de lista (três bolinhas com linhas) para o botão Histórico
    private static class IconeLista implements Icon {

        private final int tamanho;
        private final Color cor;

        IconeLista(int tamanho, Color cor) {
            this.tamanho = tamanho;
            this.cor = cor;
        }

        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);

            float espessura = tamanho / 8f;
            g2.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int bolinha = Math.round(espessura * 1.4f);
            for (int i = 0; i < 3; i++) {
                int linhaY = y + tamanho / 6 + i * tamanho / 3;
                g2.fillOval(x, linhaY - bolinha / 2, bolinha, bolinha);
                g2.drawLine(x + bolinha + tamanho / 6, linhaY, x + tamanho, linhaY);
            }

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    // Ícone de sair: porta aberta com uma seta apontando para fora
    private static class IconeSair implements Icon {

        private final int tamanho;
        private final Color cor;

        IconeSair(int tamanho, Color cor) {
            this.tamanho = tamanho;
            this.cor = cor;
        }

        @Override
        public void paintIcon(java.awt.Component c, Graphics g, int x, int y) {

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(cor);

            float espessura = tamanho / 9f;
            g2.setStroke(new BasicStroke(espessura, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            int m = Math.round(espessura);
            int t = tamanho - m;

            // Porta: um "C" quadrado aberto para a direita
            int porta = tamanho * 11 / 20;
            g2.drawPolyline(
                    new int[] { x + porta, x + m, x + m, x + porta },
                    new int[] { y + m, y + m, y + t, y + t },
                    4);

            // Seta saindo pela abertura
            int meio = y + tamanho / 2;
            int ponta = x + t;
            int aba = tamanho / 4;
            g2.drawLine(x + tamanho * 7 / 20, meio, ponta, meio);
            g2.drawLine(ponta - aba, meio - aba, ponta, meio);
            g2.drawLine(ponta - aba, meio + aba, ponta, meio);

            g2.dispose();
        }

        @Override
        public int getIconWidth() {
            return tamanho;
        }

        @Override
        public int getIconHeight() {
            return tamanho;
        }
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public JButton getBtnLogo() {
        return btnLogo;
    }

    public JButton getBtnSair() {
        return btnSair;
    }

    public JButton getBtnChat() {
        return btnChat;
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public JButton getBtnMeusLivros() {
        return btnMeusLivros;
    }

    public JButton getBtnSolicitacoes() {
        return btnSolicitacoes;
    }

    public JButton getBtnLivrosComigo() {
        return btnLivrosComigo;
    }

    public JButton getBtnPerfil() {
        return btnPerfil;
    }

    public JButton getBtnCalendario() {
        return btnCalendario;
    }

    public JButton getBtnHistorico() {
        return btnHistorico;
    }

    public JButton getBtnNotificacao() {
        return btnNotificacao;
    }
}
