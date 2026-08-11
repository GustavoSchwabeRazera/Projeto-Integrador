package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JButton;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.BorderFactory;

public class TelaSolicitacoes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Painel com cantos arredondados
     */
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

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.setColor(getBackground());

            g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radius,
                radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {

                    TelaSolicitacoes frame = new TelaSolicitacoes();
                    frame.setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public TelaSolicitacoes() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1330, 721);

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


        // =====================================================
        // BOTÃO HOME
        // =====================================================

        JButton btnHome = new JButton("");

        // =====================================================
        // TAMANHO DO BOTÃO
        // Altere esses valores para aumentar/diminuir
        // =====================================================

        btnHome.setPreferredSize(new Dimension(50, 50));


        // =====================================================
        // ÍCONE HOME
        // =====================================================

        ImageIcon iconeHome = new ImageIcon(
            TelaSolicitacoes.class.getResource("/imagens/casa 1.png")
        );

        // =====================================================
        // TAMANHO DO ÍCONE
        // Altere esses valores para aumentar/diminuir
        // =====================================================

        Image imagemHome = iconeHome.getImage().getScaledInstance(
            60,
            60,
            Image.SCALE_SMOOTH
        );

        btnHome.setIcon(new ImageIcon(imagemHome));


        // Configurações do botão
        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(new Color(10, 86, 27));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);

        btnHome.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });

        contentPane.add(
            btnHome,
            "cell 0 0,alignx left,aligny top"
        );


        // =====================================================
        // LOGO
        // =====================================================

        JLabel lblNewLabel = new JLabel("");

        lblNewLabel.setIcon(
            new ImageIcon(
                TelaSolicitacoes.class.getResource("/imagens/Logo.png")
            )
        );

        contentPane.add(
            lblNewLabel,
            "cell 0 1 5 1,alignx center"
        );


        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel lblSolicitacoes = new JLabel("Solicitações");

        lblSolicitacoes.setForeground(
            new Color(10, 86, 27)
        );

        lblSolicitacoes.setFont(
            new Font("Tahoma", Font.BOLD, 34)
        );

        contentPane.add(
            lblSolicitacoes,
            "cell 0 2 5 1,alignx center"
        );


        // =====================================================
        // PERFIL
        // =====================================================

        JButton btnPerfil = new JButton();

        btnPerfil.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });

        ImageIcon perfil = new ImageIcon(
            TelaSolicitacoes.class.getResource("/imagens/perfil3.png")
        );

        Image imgPerfil = perfil.getImage().getScaledInstance(
            70,
            70,
            Image.SCALE_SMOOTH
        );

        btnPerfil.setIcon(new ImageIcon(TelaSolicitacoes.class.getResource("/imagens/FotoPerfil.png")));
        btnPerfil.setBorderPainted(false);
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setFocusPainted(false);

        contentPane.add(
            btnPerfil,
            "cell 4 0,alignx right,aligny top"
        );


        // =====================================================
        // PAINEL DE SOLICITAÇÕES
        // =====================================================

        RoundedPanel painelSolicitacoes = new RoundedPanel(
            new MigLayout(
                "",
                "[grow]",
                "[]15[]15[]"
            ),
            40
        );

        painelSolicitacoes.setBackground(
            new Color(36, 107, 45)
        );

        painelSolicitacoes.setBorder(
            BorderFactory.createEmptyBorder(
                30,
                35,
                30,
                35
            )
        );

        contentPane.add(
            painelSolicitacoes,
            "cell 0 3 5 8,grow"
        );


        // =====================================================
        // SOLICITAÇÃO
        // =====================================================

        RoundedPanel solicitacao = new RoundedPanel(
            new MigLayout(
                "insets 14 25 14 25",
                "[]20[grow]20[40!]20[40!]",
                "[center]"
            ),
            35
        );

        solicitacao.setBackground(
            new Color(174, 244, 198)
        );

        painelSolicitacoes.add(
            solicitacao,
            "cell 0 0,growx,aligny top"
        );


        // =====================================================
        // FOTO DO USUÁRIO
        // =====================================================

        JLabel lblFoto = new JLabel();

        ImageIcon foto = new ImageIcon(
            TelaSolicitacoes.class.getResource("/imagens/perfil3.png")
        );

        Image imgFoto = foto.getImage().getScaledInstance(
            45,
            45,
            Image.SCALE_SMOOTH
        );

        lblFoto.setIcon(
            new ImageIcon(imgFoto)
        );

        solicitacao.add(
            lblFoto,
            "cell 0 0,alignx center,aligny center"
        );


        // =====================================================
        // NOME + LIVRO
        // =====================================================

        JPanel informacoes = new JPanel();

        informacoes.setOpaque(false);

        informacoes.setLayout(
            new MigLayout(
                "insets 0",
                "[grow]",
                "[]2[]"
            )
        );

        solicitacao.add(
            informacoes,
            "cell 1 0,growx,aligny center"
        );


        JLabel lblNome = new JLabel(
            "Robson Machado quer Harry Potter"
        );

        lblNome.setFont(
            new Font("Tahoma", Font.BOLD, 21)
        );

        informacoes.add(
            lblNome,
            "cell 0 0"
        );


        JLabel lblLivro = new JLabel(
            "Solicitação de empréstimo"
        );

        lblLivro.setForeground(
            new Color(60, 60, 60)
        );

        lblLivro.setFont(
            new Font("Tahoma", Font.PLAIN, 15)
        );

        informacoes.add(
            lblLivro,
            "cell 0 1"
        );


        // =====================================================
        // BOTÃO RECUSAR
        // =====================================================

        JButton btnRecusar = new JButton("X");

        btnRecusar.setFont(
            new Font("Tahoma", Font.BOLD, 28)
        );

        btnRecusar.setForeground(
            new Color(150, 30, 30)
        );

        btnRecusar.setBackground(
            new Color(174, 244, 198)
        );

        btnRecusar.setBorderPainted(false);
        btnRecusar.setContentAreaFilled(false);
        btnRecusar.setFocusPainted(false);

        btnRecusar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });

        solicitacao.add(
            btnRecusar,
            "cell 2 0,alignx center,aligny center"
        );


        // =====================================================
        // BOTÃO ACEITAR
        // =====================================================

        JButton btnAceitar = new JButton("✓");

        btnAceitar.setFont(
            new Font("Tahoma", Font.BOLD, 32)
        );

        btnAceitar.setForeground(
            new Color(20, 60, 25)
        );

        btnAceitar.setBackground(
            new Color(174, 244, 198)
        );

        btnAceitar.setBorderPainted(false);
        btnAceitar.setContentAreaFilled(false);
        btnAceitar.setFocusPainted(false);

        btnAceitar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

            }
        });

        solicitacao.add(
            btnAceitar,
            "cell 3 0,alignx center,aligny center"
        );
    }
}