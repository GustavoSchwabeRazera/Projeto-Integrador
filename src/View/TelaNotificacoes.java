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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.ByteArrayInputStream;
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
import model.Solicitacao;

public class TelaNotificacoes extends JFrame {

    private static final long serialVersionUID = 1L;

    // Tela aberta ao clicar em uma notificação
    public enum Destino { SOLICITACOES, MEUS_LIVROS, LIVROS_COMIGO }

    // Cada tipo de notificação tem sua etiqueta colorida
    private enum Tipo {
        SOLICITACAO("Solicitação", new Color(33, 99, 178)),
        EMPRESTIMO("Empréstimo", new Color(30, 125, 60)),
        PRAZO("Prazo", new Color(176, 96, 0)),
        ATRASO("Atrasado", new Color(176, 32, 32)),
        DEVOLUCAO("Devolução", new Color(106, 61, 160));

        private final String nome;
        private final Color cor;

        Tipo(String nome, Color cor) {
            this.nome = nome;
            this.cor = cor;
        }
    }

    private JPanel contentPane;
    private JButton btnHome;
    private JPanel listaNotificacoes;

    private Consumer<Solicitacao> aoConversar;
    private Consumer<EmprestimoAtivo> aoConfirmarDevolucao;
    private Consumer<EmprestimoAtivo> aoRecusarDevolucao;
    private Consumer<Destino> aoAbrirTela;

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
        contentPane.setBackground(Cores.FUNDO);
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
            new MigLayout("insets 0", "[0:0:,grow,fill]", "[0:0:,grow,fill]"), 40
        );
        painelNotificacoes.setForeground(new Color(10, 86, 27));
        painelNotificacoes.setBackground(new Color(36, 107, 45));
        painelNotificacoes.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        contentPane.add(painelNotificacoes, "cell 0 3 5 8,grow");

        // Lista dos cartões (com barra de rolagem quando tiver muitos)
        listaNotificacoes = new ListaRolavel(
            new MigLayout("insets 0, wrap 1", "[grow]", "[]15[]")
        );

        JScrollPane scroll = new JScrollPane(listaNotificacoes);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        painelNotificacoes.add(scroll, "cell 0 0,grow");

        mostrarNotificacoes(
            new ArrayList<AvisoPrazo>(),
            new ArrayList<Solicitacao>(),
            new ArrayList<EmprestimoAtivo>(),
            new ArrayList<Solicitacao>(),
            new ArrayList<EmprestimoAtivo>(),
            new ArrayList<EmprestimoAtivo>()
        );
    }


    // =====================================================
    // MOSTRA AS NOTIFICAÇÕES RECEBIDAS E AS ENVIADAS NA TELA
    // Só a frase: datas e prazos ficam nas telas Livros Comigo
    // e Solicitações, abertas ao clicar na notificação
    // =====================================================

    public void mostrarNotificacoes(
            List<AvisoPrazo> avisos,
            List<Solicitacao> pedidosRecebidos,
            List<EmprestimoAtivo> devolucoesParaConfirmar,
            List<Solicitacao> pedidosEnviados,
            List<EmprestimoAtivo> emprestimosAceitos,
            List<EmprestimoAtivo> devolucoesInformadas) {

        listaNotificacoes.removeAll();

        boolean temRecebidas = !avisos.isEmpty()
                || !pedidosRecebidos.isEmpty()
                || !devolucoesParaConfirmar.isEmpty();
        boolean temEnviadas = !pedidosEnviados.isEmpty()
                || !emprestimosAceitos.isEmpty()
                || !devolucoesInformadas.isEmpty();

        if (!temRecebidas && !temEnviadas) {

            JLabel lblVazio = new JLabel("Nenhuma notificação.");
            lblVazio.setForeground(Color.WHITE);
            lblVazio.setFont(new Font("Tahoma", Font.BOLD, 22));
            listaNotificacoes.add(lblVazio, "alignx center,gaptop 40");

        } else {

            if (temRecebidas) {
                listaNotificacoes.add(criarTituloSecao("Recebidas"));

                for (AvisoPrazo a : avisos) {
                    adicionarCartao(
                        a.isAtrasado() ? Tipo.ATRASO : Tipo.PRAZO,
                        Destino.LIVROS_COMIGO,
                        pegarIconeCalendario(),
                        a.getLembrete()
                    );
                    adicionarCartao(
                        Tipo.EMPRESTIMO,
                        Destino.LIVROS_COMIGO,
                        pegarFoto(a.getFotoDono(), 45),
                        a.getNomeDono() + " emprestou \"" + a.getTituloLivro() + "\" para você"
                    );
                }
                for (Solicitacao s : pedidosRecebidos) {
                    adicionarCartao(
                        Tipo.SOLICITACAO,
                        Destino.SOLICITACOES,
                        pegarFoto(s.getFotoSolicitante(), 45),
                        s.getNomeSolicitante() + " pediu \"" + s.getTituloLivro() + "\"",
                        criarBotaoConversar(s)
                    );
                }
                for (EmprestimoAtivo d : devolucoesParaConfirmar) {
                    adicionarCartao(
                        Tipo.DEVOLUCAO,
                        Destino.MEUS_LIVROS,
                        pegarFoto(d.getFotoLeitor(), 45),
                        d.getNomeLeitor() + " devolveu \"" + d.getTituloLivro() + "\"",
                        criarBotaoRecusar(d),
                        criarBotaoConfirmar(d)
                    );
                }
            }

            if (temEnviadas) {
                listaNotificacoes.add(criarTituloSecao("Enviadas"), temRecebidas ? "gaptop 15" : "");

                for (Solicitacao s : pedidosEnviados) {
                    adicionarCartao(
                        Tipo.SOLICITACAO,
                        Destino.SOLICITACOES,
                        pegarFoto(s.getFotoDono(), 45),
                        "Você pediu \"" + s.getTituloLivro() + "\" para " + s.getNomeDono()
                    );
                }
                for (EmprestimoAtivo e : emprestimosAceitos) {
                    adicionarCartao(
                        Tipo.EMPRESTIMO,
                        Destino.MEUS_LIVROS,
                        pegarFoto(e.getFotoLeitor(), 45),
                        "Você emprestou \"" + e.getTituloLivro() + "\" para " + e.getNomeLeitor()
                    );
                }
                for (EmprestimoAtivo d : devolucoesInformadas) {
                    adicionarCartao(
                        Tipo.DEVOLUCAO,
                        Destino.LIVROS_COMIGO,
                        pegarFoto(d.getFotoDono(), 45),
                        "Você avisou " + d.getNomeDono() + " que devolveu \"" + d.getTituloLivro() + "\""
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
    // BARRA DE UMA NOTIFICAÇÃO
    // Etiqueta colorida do tipo + foto + frase + botões (se tiver).
    // Clicar na barra abre a tela ligada à notificação.
    // =====================================================

    private void adicionarCartao(Tipo tipo, Destino destino, Image icone, String frase, JButton... botoes) {

        RoundedPanel cartao = new RoundedPanel(
            new MigLayout("insets 12 20 12 20", "[]15[]15[0:0:,grow,fill]15[]10[]", "[center]"), 35
        );
        cartao.setBackground(new Color(226, 240, 231));
        cartao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        cartao.add(criarEtiqueta(tipo), "cell 0 0,w 130!,aligny center");

        JLabel lblIcone = new JLabel(new ImageIcon(icone));
        cartao.add(lblIcone, "cell 1 0,alignx center,aligny center");

        JLabel lblFrase = new JLabel(frase);
        lblFrase.setFont(new Font("Tahoma", Font.BOLD, 21));
        cartao.add(lblFrase, "cell 2 0,aligny center");

        for (int i = 0; i < botoes.length; i++) {
            cartao.add(botoes[i], "cell " + (3 + i) + " 0,alignx center,aligny center");
        }

        cartao.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (aoAbrirTela != null) {
                    aoAbrirTela.accept(destino);
                }
            }
        });

        listaNotificacoes.add(cartao, "growx,wmin 0");
    }

    private JPanel criarEtiqueta(Tipo tipo) {

        RoundedPanel etiqueta = new RoundedPanel(
            new MigLayout("insets 6 8 6 8, align center center"), 22
        );
        etiqueta.setBackground(tipo.cor);

        JLabel lblTipo = new JLabel(tipo.nome);
        lblTipo.setForeground(Color.WHITE);
        lblTipo.setFont(new Font("Tahoma", Font.BOLD, 15));
        etiqueta.add(lblTipo);

        return etiqueta;
    }

    private JButton criarBotaoRecusar(EmprestimoAtivo d) {

        JButton botao = criarBotaoIcone("/imagens/excluirdim.png", "Não recebi");
        botao.addActionListener(e -> {
            if (aoRecusarDevolucao != null) {
                aoRecusarDevolucao.accept(d);
            }
        });

        return botao;
    }

    private JButton criarBotaoConfirmar(EmprestimoAtivo d) {

        JButton botao = criarBotaoIcone("/imagens/verificadim.png", "Confirmar devolução");
        botao.addActionListener(e -> {
            if (aoConfirmarDevolucao != null) {
                aoConfirmarDevolucao.accept(d);
            }
        });

        return botao;
    }

    private JButton criarBotaoConversar(Solicitacao s) {

        JButton botao = criarBotaoIcone(IconeChat.criar(42, 0), "Conversar");
        botao.addActionListener(e -> {
            if (aoConversar != null) {
                aoConversar.accept(s);
            }
        });

        return botao;
    }

    private JButton criarBotaoIcone(String imagem, String dica) {

        return criarBotaoIcone(new ImageIcon(TelaNotificacoes.class.getResource(imagem)), dica);
    }

    private JButton criarBotaoIcone(ImageIcon icone, String dica) {

        JButton botao = new JButton("");
        botao.setIcon(icone);
        botao.setToolTipText(dica);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return botao;
    }

    private Image pegarIconeCalendario() {

        return new ImageIcon(TelaNotificacoes.class.getResource("/imagens/calendario.png"))
                .getImage()
                .getScaledInstance(45, 45, Image.SCALE_SMOOTH);
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


    public void setAoConversar(Consumer<Solicitacao> aoConversar) {
        this.aoConversar = aoConversar;
    }

    // O controller diz o que fazer quando clicar em confirmar/recusar a devolução
    public void setAoConfirmarDevolucao(Consumer<EmprestimoAtivo> aoConfirmarDevolucao) {
        this.aoConfirmarDevolucao = aoConfirmarDevolucao;
    }

    public void setAoRecusarDevolucao(Consumer<EmprestimoAtivo> aoRecusarDevolucao) {
        this.aoRecusarDevolucao = aoRecusarDevolucao;
    }

    // O controller diz como abrir a tela ligada à notificação clicada
    public void setAoAbrirTela(Consumer<Destino> aoAbrirTela) {
        this.aoAbrirTela = aoAbrirTela;
    }
}
