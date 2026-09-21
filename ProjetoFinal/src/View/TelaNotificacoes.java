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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class TelaNotificacoes extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JButton btnHome;
    private JButton btnPerfil;
    private JButton btnExcluir;
    private JButton btnAceitar;
    private RoundedPanel solicitacaoItem;

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
            new MigLayout("", "[grow]", "[]15[]15[]"), 40
        );
        painelNotificacoes.setForeground(new Color(10, 86, 27));
        painelNotificacoes.setBackground(new Color(36, 107, 45));
        painelNotificacoes.setBorder(BorderFactory.createEmptyBorder(30, 35, 30, 35));
        contentPane.add(painelNotificacoes, "cell 0 3 5 8,grow");

        // ITEM DE NOTIFICAÇÃO (card clicável)
        // 4 colunas: 0 = foto | 1 = textos | 2 = excluir | 3 = aceitar
        solicitacaoItem = new RoundedPanel(
            new MigLayout("insets 14 25 14 25", "[]20[grow]20[40!]20[40!]", "[center]"), 35
        );
        solicitacaoItem.setForeground(new Color(255, 255, 255));
        solicitacaoItem.setBackground(new Color(174, 244, 198));
        solicitacaoItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        painelNotificacoes.add(solicitacaoItem, "cell 0 0,growx,aligny top");

        // FOTO DO USUÁRIO
        JLabel lblFoto = new JLabel();
        ImageIcon foto = new ImageIcon(TelaNotificacoes.class.getResource("/imagens/perfil3.png"));
        Image imgFoto = foto.getImage().getScaledInstance(45, 45, Image.SCALE_SMOOTH);
        lblFoto.setIcon(new ImageIcon(imgFoto));
        solicitacaoItem.add(lblFoto, "cell 0 0,alignx center,aligny center");

        // NOME + TEXTO
        JPanel informacoes = new JPanel();
        informacoes.setOpaque(false);
        informacoes.setLayout(new MigLayout("insets 0", "[grow]", "[]2[]"));
        solicitacaoItem.add(informacoes, "cell 1 0,growx,aligny center");

        JLabel lblNome = new JLabel("Robson Machado quer Harry Potter");
        lblNome.setFont(new Font("Tahoma", Font.BOLD, 21));
        informacoes.add(lblNome, "cell 0 0");

        JLabel lblLivro = new JLabel("Solicitação de empréstimo");
        lblLivro.setForeground(new Color(60, 60, 60));
        lblLivro.setFont(new Font("Tahoma", Font.PLAIN, 15));
        informacoes.add(lblLivro, "cell 0 1");

        // BOTÃO EXCLUIR
        btnExcluir = new JButton("");
        btnExcluir.setIcon(new ImageIcon(TelaNotificacoes.class.getResource("/imagens/excluirdim.png")));
        btnExcluir.setBorderPainted(false);
        btnExcluir.setContentAreaFilled(false);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        solicitacaoItem.add(btnExcluir, "cell 2 0,alignx center,aligny center");

        // BOTÃO ACEITAR
        btnAceitar = new JButton("");
        btnAceitar.setIcon(new ImageIcon(TelaNotificacoes.class.getResource("/imagens/verificadim.png")));
        btnAceitar.setFont(new Font("Tahoma", Font.BOLD, 32));
        btnAceitar.setForeground(new Color(20, 60, 25));
        btnAceitar.setBackground(new Color(174, 244, 198));
        btnAceitar.setBorderPainted(false);
        btnAceitar.setContentAreaFilled(false);
        btnAceitar.setFocusPainted(false);
        btnAceitar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        solicitacaoItem.add(btnAceitar, "cell 3 0,alignx center,aligny center");
    }

    public JButton getBtnHome()    { return btnHome; }
    public JButton getBtnPerfil()  { return btnPerfil; }
    public JButton getBtnExcluir() { return btnExcluir; }
    public JButton getBtnAceitar() { return btnAceitar; }

    /** Painel do card, caso o Controller precise dele. */
    public JPanel getCardSolicitacao() { return solicitacaoItem; }

    /**
     * Registra a ação executada ao clicar no card da notificação.
     * Os botões aceitar/excluir são ignorados: eles tratam o próprio clique.
     *
     * Uso no Controller:
     *   telaNotificacoes.addNotificacaoClickListener(e -> abrirSolicitacoes());
     */
    public void addNotificacaoClickListener(final ActionListener acao) {
        MouseAdapter adapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                acao.actionPerformed(
                    new ActionEvent(solicitacaoItem, ActionEvent.ACTION_PERFORMED, "notificacao")
                );
            }
        };
        aplicarListener(solicitacaoItem, adapter);
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
}