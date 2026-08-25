package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

public class TelaCriarConta extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTextField txtNome;
    private JTextField txtEmail;

    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;

    private JButton botaoCadastrar;
    private JButton botaoEntrar;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {

                    TelaCriarConta frame = new TelaCriarConta();
                    frame.setVisible(true);

                } catch (Exception e) {

                    e.printStackTrace();

                }
            }
        });
    }

    // ======================================
    // GETTERS
    // ======================================

    public JTextField getTxtNome() {
        return txtNome;
    }

    public JTextField getTxtEmail() {
        return txtEmail;
    }

    public JPasswordField getTxtSenha() {
        return txtSenha;
    }

    public JPasswordField getTxtConfirmarSenha() {
        return txtConfirmarSenha;
    }

    public JButton getBotaoCadastrar() {
        return botaoCadastrar;
    }

    public JButton getBotaoEntrar() {
        return botaoEntrar;
    }

    // ======================================
    // CONSTRUTOR
    // ======================================

    public TelaCriarConta() {

        setTitle("Capas Vivas");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setSize(1920, 1080);

        setLocationRelativeTo(null);

        // ======================================
        // CONTENT PANE
        // ======================================

        contentPane = new JPanel();

        contentPane.setBackground(
            new Color(175, 244, 198)
        );

        contentPane.setBorder(
            new EmptyBorder(0, 0, 0, 0)
        );

        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout(
                "fill, insets 0",
                "[grow]",
                "[grow]"
            )
        );

        // ======================================
        // PAINEL CENTRAL
        // ======================================

        JPanel panelCadastro = new ImagePanelLogin();

        panelCadastro.setOpaque(false);

        panelCadastro.setPreferredSize(
            new java.awt.Dimension(520, 700)
        );

        contentPane.add(
            panelCadastro,
            "cell 0 0,align center"
        );

        panelCadastro.setLayout(
            new MigLayout(
                "insets 30 45 30 45, fillx",
                "[grow]",
                "[]10[]8[]18[]5[]15[]5[]15[]5[]15[]8[]20[]10[]"
            )
        );

        // ======================================
        // LOGO
        // ======================================

        JLabel lblLogo = new JLabel();

        ImageIcon logoOriginal = new ImageIcon(
            TelaCriarConta.class.getResource(
                "/imagens/Logo.png"
            )
        );

        Image logoRedimensionada =
            logoOriginal.getImage().getScaledInstance(
                180,
                90,
                Image.SCALE_SMOOTH
            );

        lblLogo.setIcon(
            new ImageIcon(logoRedimensionada)
        );

        panelCadastro.add(
            lblLogo,
            "cell 0 0,align center"
        );

        // ======================================
        // TÍTULO
        // ======================================

        JLabel lblTitulo =
            new JLabel("Criar sua conta");

        lblTitulo.setForeground(
            new Color(20, 90, 40)
        );

        lblTitulo.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                30
            )
        );

        panelCadastro.add(
            lblTitulo,
            "cell 0 1,align center"
        );

        // ======================================
        // SUBTÍTULO
        // ======================================

        JLabel lblSub =
            new JLabel("Preencha seus dados para começar");

        lblSub.setForeground(
            new Color(90, 90, 90)
        );

        lblSub.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                16
            )
        );

        panelCadastro.add(
            lblSub,
            "cell 0 2,align center"
        );

        // ======================================
        // NOME
        // ======================================

        JLabel lblNome =
            new JLabel("Nome");

        lblNome.setForeground(
            new Color(30, 30, 30)
        );

        lblNome.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                15
            )
        );

        panelCadastro.add(
            lblNome,
            "cell 0 3"
        );

        txtNome = new JTextField();

        txtNome.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                16
            )
        );

        panelCadastro.add(
            txtNome,
            "cell 0 4,growx,h 40!"
        );

        // ======================================
        // E-MAIL
        // ======================================

        JLabel lblEmail =
            new JLabel("E-mail");

        lblEmail.setForeground(
            new Color(30, 30, 30)
        );

        lblEmail.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                15
            )
        );

        panelCadastro.add(
            lblEmail,
            "cell 0 5"
        );

        txtEmail = new JTextField();

        txtEmail.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                16
            )
        );

        panelCadastro.add(
            txtEmail,
            "cell 0 6,growx,h 40!"
        );

        // ======================================
        // SENHA
        // ======================================

        JLabel lblSenha =
            new JLabel("Senha");

        lblSenha.setForeground(
            new Color(30, 30, 30)
        );

        lblSenha.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                15
            )
        );

        panelCadastro.add(
            lblSenha,
            "cell 0 7"
        );

        txtSenha = new JPasswordField();

        txtSenha.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                16
            )
        );

        panelCadastro.add(
            txtSenha,
            "cell 0 8,growx,h 40!"
        );

        // ======================================
        // CONFIRMAR SENHA
        // ======================================

        JLabel lblConfirmarSenha =
            new JLabel("Confirmar senha");

        lblConfirmarSenha.setForeground(
            new Color(30, 30, 30)
        );

        lblConfirmarSenha.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                15
            )
        );

        panelCadastro.add(
            lblConfirmarSenha,
            "cell 0 9"
        );

        txtConfirmarSenha =
            new JPasswordField();

        txtConfirmarSenha.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                16
            )
        );

        panelCadastro.add(
            txtConfirmarSenha,
            "cell 0 10,growx,h 40!"
        );

        // ======================================
        // MOSTRAR SENHA
        // ======================================

        JCheckBox chkMostrar =
            new JCheckBox("Mostrar senha");

        chkMostrar.setOpaque(false);

        chkMostrar.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                13
            )
        );

        chkMostrar.addActionListener(e -> {

            if (chkMostrar.isSelected()) {

                txtSenha.setEchoChar((char) 0);

                txtConfirmarSenha.setEchoChar((char) 0);

            } else {

                txtSenha.setEchoChar('•');

                txtConfirmarSenha.setEchoChar('•');

            }
        });

        panelCadastro.add(
            chkMostrar,
            "cell 0 11"
        );

        // ======================================
        // BOTÃO CADASTRAR
        // ======================================

        Color verde =
            new Color(24, 125, 45);

        Color hover =
            new Color(16, 100, 35);

        botaoCadastrar =
            new JButton("Cadastrar");

        botaoCadastrar.setBackground(verde);

        botaoCadastrar.setForeground(
            Color.WHITE
        );

        botaoCadastrar.setFocusPainted(false);

        botaoCadastrar.setBorderPainted(false);

        botaoCadastrar.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                18
            )
        );

        botaoCadastrar.setBorder(
            BorderFactory.createLineBorder(
                new Color(24, 125, 45),
                2,
                true
            )
        );

        botaoCadastrar.addMouseListener(
            new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(
                    java.awt.event.MouseEvent e
                ) {

                    botaoCadastrar.setBackground(
                        hover
                    );
                }

                @Override
                public void mouseExited(
                    java.awt.event.MouseEvent e
                ) {

                    botaoCadastrar.setBackground(
                        verde
                    );
                }
            }
        );

        panelCadastro.add(
            botaoCadastrar,
            "cell 0 12,growx,h 50!"
        );

        // ======================================
        // JÁ POSSUI CONTA
        // ======================================

        JLabel lblJaPossui =
            new JLabel("Já possui uma conta?");

        lblJaPossui.setForeground(
            new Color(110, 110, 110)
        );

        lblJaPossui.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                14
            )
        );

        panelCadastro.add(
            lblJaPossui,
            "cell 0 13,alignx center"
        );

        // ======================================
        // BOTÃO ENTRAR
        // ======================================

        botaoEntrar =
            new JButton("<HTML><U>Entrar</U></HTML>");

        botaoEntrar.setForeground(
            new Color(24, 125, 45)
        );

        botaoEntrar.setFont(
            new Font(
                "Segoe UI",
                Font.BOLD,
                15
            )
        );

        botaoEntrar.setBorderPainted(false);

        botaoEntrar.setContentAreaFilled(false);

        botaoEntrar.setFocusPainted(false);

        panelCadastro.add(
            botaoEntrar,
            "cell 0 14,alignx center"
        );
    }
}