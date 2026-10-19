package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Component;
import java.awt.Container;
import java.awt.Color;
import java.awt.Font;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import model.AvisoPrazo;
import model.Solicitacao;

public class TelaNotificacoes extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JPanel contentPane;
    private JButton btnHome;
    private JButton btnPerfil;
    private JPanel listaNotificacoes;

    private Consumer<Solicitacao> aoAceitar;
    private Consumer<Solicitacao> aoRecusar;
    private Consumer<Solicitacao> aoAbrirSolicitacao;

    /** Painel com cantos arredondados. */
    private static class RoundedPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final int radius;

        public RoundedPanel(LayoutManager layout, int radius) {
            super(layout);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    TelaNotificacoes frame = new TelaNotificacoes();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public TelaNotificacoes() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout(
                "",
                "[200,grow][200,grow][200,grow][200,grow][200,grow]",
                "[][113.00,grow][][grow][grow][grow][grow][grow][grow][grow][grow]"
            )
        );

        // BOTÃO HOME
        btnHome = new JButton("");
        btnHome.setPreferredSize(new Dimension(50, 50));
        ImageIcon iconeHome = new ImageIcon(TelaNotificacoes.class.getResource("/imagens/casa 1.png"));
        Image imagemHome = iconeHome.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        btnHome.setIcon(new ImageIcon(imagemHome));
        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(new Color(10, 86, 27));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

        // PERFIL
        btnPerfil = new JButton();
        btnPerfil.setIcon(new ImageIcon(TelaNotificacoes.class.getResource("/imagens/FotoPerfil.png")));
        btnPerfil.setBorderPainted(false);
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setFocusPainted(false);
        btnPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnPerfil, "cell 4 0,alignx right,aligny top");

        // LOGO
        JLabel lblLogo = new JLabel("");
        lblLogo.setIcon(new ImageIcon(TelaNotificacoes.class.getResource("/imagens/Logo.png")));
        contentPane.add(lblLogo, "cell 0 1 5 1,alignx center");

        // TÍTULO
        JLabel lblNotificacoes = new JLabel("Notificações");
        lblNotificacoes.setForeground(new Color(10, 86, 27));
        lblNotificacoes.setFont(new Font("Tahoma", Font.BOLD, 34));
        contentPane.add(lblNotificacoes, "cell 0 2 5 1,alignx center");

        // PAINEL DE NOTIFICAÇÕES
        RoundedPanel painelNotificacoes = new RoundedPanel(
            new MigLayout("insets 0", "[grow]", "[grow]"), 40
        );
        painelNotificacoes.setForeground(new Color(10, 86, 27));
        painelNotificacoes.setBackground(new Color(36, 107, 45));
        painelNotificacoes.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        contentPane.add(painelNotificacoes, "cell 0 3 5 8,grow");

        // Lista dos cartões (com barra de rolagem quando tiver muitos)
        listaNotificacoes = new JPanel(
            new MigLayout("insets 0, wrap 1", "[grow]", "[]15[]")
        );
        listaNotificacoes.setOpaque(false);

        JScrollPane scroll = new JScrollPane(listaNotificacoes);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        painelNotificacoes.add(scroll, "cell 0 0,grow");

        mostrarNotificacoes(new ArrayList<AvisoPrazo>(), new ArrayList<Solicitacao>());
    }


    // =====================================================
    // MOSTRA OS AVISOS DE PRAZO E AS SOLICITAÇÕES NA TELA
    // =====================================================

    public void mostrarNotificacoes(List<AvisoPrazo> avisos, List<Solicitacao> solicitacoes) {

        listaNotificacoes.removeAll();

        if (avisos.isEmpty() && solicitacoes.isEmpty()) {

            JLabel lblVazio = new JLabel("Nenhuma notificação.");
            lblVazio.setForeground(Color.WHITE);
            lblVazio.setFont(new Font("Tahoma", Font.BOLD, 22));
            listaNotificacoes.add(lblVazio, "alignx center,gaptop 40");

        } else {

            if (!avisos.isEmpty()) {
                listaNotificacoes.add(criarTituloSecao("Empréstimos perto do fim"));
                for (AvisoPrazo a : avisos) {
                    listaNotificacoes.add(criarCartaoAviso(a), "growx");
                }
            }

            if (!solicitacoes.isEmpty()) {
                listaNotificacoes.add(
                    criarTituloSecao("Solicitações de empréstimo"),
                    avisos.isEmpty() ? "" : "gaptop 15"
                );
                for (Solicitacao s : solicitacoes) {
                    listaNotificacoes.add(criarCartaoSolicitacao(s), "growx");
                }
            }
        }

        listaNotificacoes.revalidate();
        listaNotificacoes.repaint();
    }

    private JLabel criarTituloSecao(String texto) {

        JLabel lblTitulo = new JLabel(texto);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));

        return lblTitulo;
    }


    // =====================================================
    // CARTÃO DE AVISO: EMPRÉSTIMO PERTO DE ACABAR
    // =====================================================

    private JPanel criarCartaoAviso(AvisoPrazo a) {

        RoundedPanel cartao = new RoundedPanel(
            new MigLayout("insets 14 25 14 25", "[]20[grow]", "[center]"), 35
        );
        cartao.setBackground(new Color(255, 236, 179));

        // Ícone do calendário
        ImageIcon calendario = new ImageIcon(TelaNotificacoes.class.getResource("/imagens/calendario.png"));
        JLabel lblIcone = new JLabel(
            new ImageIcon(calendario.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH))
        );
        cartao.add(lblIcone, "cell 0 0,alignx center,aligny center");

        // Livro + prazo
        JPanel informacoes = new JPanel(new MigLayout("insets 0", "[grow]", "[]2[]"));
        informacoes.setOpaque(false);
        cartao.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblTitulo = new JLabel(
            "Devolva \"" + a.getTituloLivro() + "\" " + textoPrazo(a.getDiasRestantes())
        );
        lblTitulo.setForeground(new Color(120, 60, 0));
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblTitulo, "cell 0 0");

        JLabel lblDetalhes = new JLabel(
            "Empréstimo de " + a.getNomeDono()
            + "   •   termina em " + a.getDataTermino().format(FORMATO_DATA)
        );
        lblDetalhes.setForeground(new Color(60, 60, 60));
        lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblDetalhes, "cell 0 1");

        return cartao;
    }

    private String textoPrazo(long dias) {

        if (dias <= 0) {
            return "hoje!";
        }
        if (dias == 1) {
            return "até amanhã";
        }
        return "em " + dias + " dias";
    }


    // =====================================================
    // CARTÃO DE SOLICITAÇÃO DE EMPRÉSTIMO
    // =====================================================

    private JPanel criarCartaoSolicitacao(Solicitacao s) {

        // 4 colunas: 0 = foto | 1 = textos | 2 = excluir | 3 = aceitar
        RoundedPanel cartao = new RoundedPanel(
            new MigLayout("insets 14 25 14 25", "[]20[grow]20[40!]20[40!]", "[center]"), 35
        );
        cartao.setBackground(new Color(174, 244, 198));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Foto de quem pediu
        JLabel lblFoto = new JLabel(new ImageIcon(pegarFoto(s.getFotoSolicitante(), 45)));
        cartao.add(lblFoto, "cell 0 0,alignx center,aligny center");

        // Nome + livro
        JPanel informacoes = new JPanel(new MigLayout("insets 0", "[grow]", "[]2[]"));
        informacoes.setOpaque(false);
        cartao.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblNome = new JLabel(s.getNomeSolicitante() + " quer " + s.getTituloLivro());
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblNome, "cell 0 0");

        JLabel lblDetalhes = new JLabel(
            "Solicitação de empréstimo em " + s.getDataPedido().format(FORMATO_DATA)
        );
        lblDetalhes.setForeground(new Color(60, 60, 60));
        lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblDetalhes, "cell 0 1");

        // Botão recusar
        JButton btnExcluir = criarBotaoIcone("/imagens/excluirdim.png", "Recusar");
        btnExcluir.addActionListener(e -> {
            if (aoRecusar != null) {
                aoRecusar.accept(s);
            }
        });
        cartao.add(btnExcluir, "cell 2 0,alignx center,aligny center");

        // Botão aceitar
        JButton btnAceitar = criarBotaoIcone("/imagens/verificadim.png", "Aceitar");
        btnAceitar.addActionListener(e -> {
            if (aoAceitar != null) {
                aoAceitar.accept(s);
            }
        });
        cartao.add(btnAceitar, "cell 3 0,alignx center,aligny center");

        // Clicar no cartão (fora dos botões) abre a tela de Solicitações
        aplicarListener(cartao, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (aoAbrirSolicitacao != null) {
                    aoAbrirSolicitacao.accept(s);
                }
            }
        });

        return cartao;
    }

    private JButton criarBotaoIcone(String imagem, String dica) {

        JButton botao = new JButton("");
        botao.setIcon(new ImageIcon(TelaNotificacoes.class.getResource(imagem)));
        botao.setToolTipText(dica);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
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
            imagem = new ImageIcon(TelaNotificacoes.class.getResource("/imagens/perfil3.png")).getImage();
        }

        return imagem.getScaledInstance(tamanho, tamanho, Image.SCALE_SMOOTH);
    }

    /** Aplica o listener no container e em todos os filhos, exceto botões. */
    private void aplicarListener(Container container, MouseListener listener) {
        container.addMouseListener(listener);
        for (Component comp : container.getComponents()) {
            if (comp instanceof AbstractButton) {
                continue;
            }
            if (comp instanceof Container) {
                aplicarListener((Container) comp, listener);
            } else {
                comp.addMouseListener(listener);
            }
        }
    }

    public JButton getBtnHome()    { return btnHome; }
    public JButton getBtnPerfil()  { return btnPerfil; }

    // O controller diz o que fazer quando clicar em aceitar/recusar/no cartão
    public void setAoAceitar(Consumer<Solicitacao> aoAceitar) {
        this.aoAceitar = aoAceitar;
    }

    public void setAoRecusar(Consumer<Solicitacao> aoRecusar) {
        this.aoRecusar = aoRecusar;
    }

    public void setAoAbrirSolicitacao(Consumer<Solicitacao> aoAbrirSolicitacao) {
        this.aoAbrirSolicitacao = aoAbrirSolicitacao;
    }
}
