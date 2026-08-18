package View;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Image;
import javax.swing.JTextField;



public class TelaLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JPasswordField txtSenha;
	
	
	

	private JButton botaoCadastrar;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TelaLogin frame = new TelaLogin();
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
    public JButton getBotaoEntrar() {
        return botaoCadastrar;
    }

	public TelaLogin() {

		setTitle("Capas Vivas");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1920, 1080);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(175, 244, 198));
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);

		// Apenas uma célula ocupando toda a tela
		contentPane.setLayout(new MigLayout(
				"fill, insets 0",
				"[grow]",
				"[grow]"));

		// ===============================
		// PAINEL CENTRAL
		// ===============================

		JPanel panelLogin = new ImagePanelLogin();
		panelLogin.setOpaque(false);

		panelLogin.setPreferredSize(new java.awt.Dimension(520, 620));

		contentPane.add(panelLogin, "cell 0 0,align center");

		panelLogin.setLayout(new MigLayout("insets 35 45 35 45, fillx", "[grow]", "[]20[]10[]25[]8[]20[]8[]30[]20[]20[]10[]"));

		// ======================================
		// LOGO
		// ======================================

		JLabel lblLogo = new JLabel();

		ImageIcon logoOriginal = new ImageIcon(
				TelaLogin.class.getResource("/imagens/Logo.png"));

		Image logoRedimensionada = logoOriginal.getImage().getScaledInstance(
				220,
				110,
				Image.SCALE_SMOOTH);

		lblLogo.setIcon(new ImageIcon(logoRedimensionada));

		panelLogin.add(lblLogo, "cell 0 0,align center");

		// ======================================
		// TITULO
		// ======================================

		JLabel lblTitulo = new JLabel("Bem-vindo!");

		lblTitulo.setForeground(new Color(20, 90, 40));
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 34));

		panelLogin.add(lblTitulo, "cell 0 1,align center");

		// ======================================
		// SUBTITULO
		// ======================================

		JLabel lblSub = new JLabel("Faça login para continuar");

		lblSub.setForeground(new Color(90, 90, 90));
		lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 18));

		panelLogin.add(lblSub, "cell 0 2,align center");
		
		// ======================================
		// E-MAIL
		// ======================================

		JLabel lblEmail = new JLabel("E-mail");
		lblEmail.setForeground(new Color(30, 30, 30));
		lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 16));

		panelLogin.add(lblEmail, "cell 0 3");

		txtNome = new JTextField();
		txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		txtNome.setColumns(10);

		panelLogin.add(txtNome, "cell 0 4,growx,h 42!");

		// ======================================
		// SENHA
		// ======================================

		JLabel lblSenha = new JLabel("Senha");
		lblSenha.setForeground(new Color(30, 30, 30));
		lblSenha.setFont(new Font("Segoe UI", Font.BOLD, 16));

		panelLogin.add(lblSenha, "cell 0 5");

		txtSenha = new JPasswordField();
		txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 16));

		panelLogin.add(txtSenha, "cell 0 6,growx,h 42!");

		JCheckBox chkMostrar = new JCheckBox("Mostrar senha");
		chkMostrar.setOpaque(false);
		chkMostrar.setBackground(new Color(0,0,0,0));
		chkMostrar.setFont(new Font("Segoe UI", Font.PLAIN, 13));

		chkMostrar.addActionListener(e -> {

		    if(chkMostrar.isSelected()) {

		        txtSenha.setEchoChar((char)0);

		    }else {

		        txtSenha.setEchoChar('•');

		    }

		});

		panelLogin.add(chkMostrar,"cell 0 7");

		// ======================================
		// BOTÃO ENTRAR
		// ======================================

		Color verde = new Color(24, 125, 45);
		Color hover = new Color(16, 100, 35);

		botaoCadastrar = new JButton("Entrar");
		botaoCadastrar.setBorderPainted(false);

		botaoCadastrar.setBackground(verde);
		botaoCadastrar.setForeground(Color.WHITE);
		botaoCadastrar.setFocusPainted(false);
		botaoCadastrar.setBorder(new LineBorder(new Color(24,125,45), 1, true));
		botaoCadastrar.setFont(new Font("Segoe UI", Font.BOLD, 18));
		botaoCadastrar.setBorder(
			    BorderFactory.createLineBorder(new Color(24,125,45), 2, true)
			);
		

		botaoCadastrar.addMouseListener(new java.awt.event.MouseAdapter() {

		    @Override
		    public void mouseEntered(java.awt.event.MouseEvent e) {
		        botaoCadastrar.setBackground(hover);
		    }

		    @Override
		    public void mouseExited(java.awt.event.MouseEvent e) {
		        botaoCadastrar.setBackground(verde);
		    }

		});
		
		panelLogin.add(botaoCadastrar, "cell 0 8,growx,h 50!");
		
	
	

		// ======================================
		// NÃO POSSUI CONTA
		// ======================================

		JLabel lblConta = new JLabel("Ainda não possui cadastro?");
		lblConta.setForeground(new Color(110, 110, 110));
		lblConta.setFont(new Font("Segoe UI", Font.PLAIN, 14));

		panelLogin.add(lblConta, "cell 0 9,alignx center");

		// ======================================
		// LINK CADASTRO
		// ======================================

		JLabel lblCadastro = new JLabel("<HTML><U>Criar conta</U></HTML>");
		lblCadastro.setForeground(new Color(24, 125, 45));
		lblCadastro.setFont(new Font("Segoe UI", Font.BOLD, 15));

		panelLogin.add(lblCadastro, "cell 0 10,alignx center");
	}}