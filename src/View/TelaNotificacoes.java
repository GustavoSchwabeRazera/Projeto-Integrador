package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.Color;
import java.awt.Font;

import java.io.ByteArrayInputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
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
import model.EmprestimoAtivo;

public class TelaNotificacoes extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private JPanel contentPane;
    private JButton btnHome;
    private JPanel listaNotificacoes;

    private Consumer<EmprestimoAtivo> aoConfirmarDevolucao;
    private Consumer<EmprestimoAtivo> aoRecusarDevolucao;

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
        Image imagemHome = iconeHome.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        btnHome.setIcon(new ImageIcon(imagemHome));
        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(new Color(10, 86, 27));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

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

        mostrarNotificacoes(
            new ArrayList<AvisoPrazo>(),
            new ArrayList<EmprestimoAtivo>(),
            new ArrayList<EmprestimoAtivo>()
        );
    }


    // =====================================================
    // MOSTRA AS NOTIFICAÇÕES RECEBIDAS E AS ENVIADAS NA TELA
    // =====================================================

    public void mostrarNotificacoes(
            List<AvisoPrazo> avisos,
            List<EmprestimoAtivo> devolucoesParaConfirmar,
            List<EmprestimoAtivo> devolucoesInformadas) {

        listaNotificacoes.removeAll();

        boolean temRecebidas = !avisos.isEmpty() || !devolucoesParaConfirmar.isEmpty();
        boolean temEnviadas = !devolucoesInformadas.isEmpty();

        if (!temRecebidas && !temEnviadas) {

            JLabel lblVazio = new JLabel("Nenhuma notificação.");
            lblVazio.setForeground(Color.WHITE);
            lblVazio.setFont(new Font("Tahoma", Font.BOLD, 22));
            listaNotificacoes.add(lblVazio, "alignx center,gaptop 40");

        } else {

            if (temRecebidas) {
                listaNotificacoes.add(criarTituloSecao("Recebidas"));

                for (AvisoPrazo a : avisos) {
                    listaNotificacoes.add(criarCartaoAviso(a), "growx");
                }
                for (EmprestimoAtivo d : devolucoesParaConfirmar) {
                    listaNotificacoes.add(criarCartaoDevolucao(d), "growx");
                }
            }

            if (temEnviadas) {
                listaNotificacoes.add(criarTituloSecao("Enviadas"), temRecebidas ? "gaptop 15" : "");

                for (EmprestimoAtivo d : devolucoesInformadas) {
                    listaNotificacoes.add(
                        criarCartaoEnviada(
                            d.getFotoDono(),
                            "Você avisou " + d.getNomeDono() + " que devolveu \"" + d.getTituloLivro() + "\"",
                            "Aviso enviado em " + d.getDevolucaoSolicitada().format(FORMATO_DATA)
                            + "   •   aguardando confirmação"
                        ),
                        "growx"
                    );
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
    // CARTÃO DE DEVOLUÇÃO ESPERANDO O DONO CONFIRMAR
    // =====================================================

    private JPanel criarCartaoDevolucao(EmprestimoAtivo d) {

        // 4 colunas: 0 = foto | 1 = textos | 2 = recusar | 3 = confirmar
        RoundedPanel cartao = new RoundedPanel(
            new MigLayout("insets 14 25 14 25", "[]20[grow]20[]10[]", "[center]"), 35
        );
        cartao.setBackground(new Color(174, 244, 198));

        // Foto de quem devolveu
        JLabel lblFoto = new JLabel(new ImageIcon(pegarFoto(d.getFotoLeitor(), 45)));
        cartao.add(lblFoto, "cell 0 0,alignx center,aligny center");

        // Nome + livro
        JPanel informacoes = new JPanel(new MigLayout("insets 0", "[grow]", "[]2[]"));
        informacoes.setOpaque(false);
        cartao.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblNome = new JLabel(d.getNomeLeitor() + " devolveu " + d.getTituloLivro());
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblNome, "cell 0 0");

        JLabel lblDetalhes = new JLabel(
            "Devolução informada em " + d.getDevolucaoSolicitada().format(FORMATO_DATA)
            + "   •   confirme se você recebeu o livro"
        );
        lblDetalhes.setForeground(new Color(60, 60, 60));
        lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblDetalhes, "cell 0 1");

        // Botão recusar
        JButton btnRecusar = criarBotaoIcone("/imagens/excluirdim.png", "Não recebi");
        btnRecusar.addActionListener(e -> {
            if (aoRecusarDevolucao != null) {
                aoRecusarDevolucao.accept(d);
            }
        });
        cartao.add(btnRecusar, "cell 2 0,alignx center,aligny center");

        // Botão confirmar
        JButton btnConfirmar = criarBotaoIcone("/imagens/verificadim.png", "Confirmar devolução");
        btnConfirmar.addActionListener(e -> {
            if (aoConfirmarDevolucao != null) {
                aoConfirmarDevolucao.accept(d);
            }
        });
        cartao.add(btnConfirmar, "cell 3 0,alignx center,aligny center");

        return cartao;
    }


    // =====================================================
    // CARTÃO DE NOTIFICAÇÃO ENVIADA (SÓ INFORMATIVO, SEM BOTÕES)
    // =====================================================

    private JPanel criarCartaoEnviada(byte[] fotoDestinatario, String titulo, String detalhes) {

        RoundedPanel cartao = new RoundedPanel(
            new MigLayout("insets 14 25 14 25", "[]20[grow]", "[center]"), 35
        );
        cartao.setBackground(new Color(226, 240, 231));

        // Foto de quem recebeu
        JLabel lblFoto = new JLabel(new ImageIcon(pegarFoto(fotoDestinatario, 45)));
        cartao.add(lblFoto, "cell 0 0,alignx center,aligny center");

        // O que foi enviado + situação
        JPanel informacoes = new JPanel(new MigLayout("insets 0", "[grow]", "[]2[]"));
        informacoes.setOpaque(false);
        cartao.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblTitulo, "cell 0 0");

        JLabel lblDetalhes = new JLabel(detalhes);
        lblDetalhes.setForeground(new Color(60, 60, 60));
        lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblDetalhes, "cell 0 1");

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

    public JButton getBtnHome()    { return btnHome; }


    // O controller diz o que fazer quando clicar em confirmar/recusar a devolução
    public void setAoConfirmarDevolucao(Consumer<EmprestimoAtivo> aoConfirmarDevolucao) {
        this.aoConfirmarDevolucao = aoConfirmarDevolucao;
    }

    public void setAoRecusarDevolucao(Consumer<EmprestimoAtivo> aoRecusarDevolucao) {
        this.aoRecusarDevolucao = aoRecusarDevolucao;
    }
}
