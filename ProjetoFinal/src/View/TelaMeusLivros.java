package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;

import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TelaMeusLivros extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnHome;
	private JButton btnPerfil;
	private JButton btnMostrarMais;

	// =====================================================
	// PAINEL COM CANTOS ARREDONDADOS
	// =====================================================

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

	// =====================================================
	// MAIN
	// =====================================================

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {

					TelaMeusLivros frame = new TelaMeusLivros();
					frame.setVisible(true);

				} catch (Exception e) {

					e.printStackTrace();

				}
			}
		});
	}

	// =====================================================
	// CONSTRUTOR
	// =====================================================

	public TelaMeusLivros() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBounds(100, 100, 1920, 1080);

		// =====================================================
		// FUNDO DA TELA
		// =====================================================

		contentPane = new JPanel();

		contentPane.setBackground(new Color(175, 244, 198));

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		// =====================================================
		// MIGLAYOUT PRINCIPAL
		// =====================================================

		contentPane.setLayout(new MigLayout("", "[200,grow][344.00,grow][561.00,grow][200,grow][200,grow]",
				"[113.00,grow][][grow][grow][grow][grow][grow][grow][grow][grow]"));

		// =====================================================
		// BOTÃO HOME
		// =====================================================

		btnHome = new JButton("");

		btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));

		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));

		btnHome.setForeground(new Color(10, 86, 27));

		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setFocusPainted(false);

		contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

		// =====================================================
		// PERFIL
		// =====================================================

		btnPerfil = new JButton();

		ImageIcon perfil = new ImageIcon(TelaMeusLivros.class.getResource("/imagens/perfil3.png"));

		Image imgPerfil = perfil.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

		btnPerfil.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/FotoPerfil.png")));

		btnPerfil.setBorderPainted(false);
		btnPerfil.setContentAreaFilled(false);
		btnPerfil.setFocusPainted(false);

		contentPane.add(btnPerfil, "cell 4 0,alignx right,aligny top");

		// =====================================================
		// TÍTULO
		// =====================================================

		JLabel lblMeusLivros = new JLabel("Meus Livros");

		lblMeusLivros.setForeground(new Color(10, 86, 27));

		lblMeusLivros.setFont(new Font("Tahoma", Font.BOLD, 34));

		contentPane.add(lblMeusLivros, "cell 0 1 5 1,alignx center");

		// PAINEL VERDE DOS LIVROS

		RoundedPanel painelLivros = new RoundedPanel(

				new MigLayout("insets 20 35 25 35", "[grow][grow][grow][grow][grow][grow]", "[grow][]"),

				40);

		painelLivros.setBackground(new Color(36, 107, 45));

		painelLivros.setBorder(BorderFactory.createEmptyBorder(20, 35, 25, 35));

		contentPane.add(painelLivros, "cell 0 2 5 8,grow");

		// =====================================================
		// LIVRO 1
		// =====================================================

		JLabel livro1 = criarLivro("/imagens/livroDemo.png");

		painelLivros.add(livro1, "cell 0 0,alignx center,aligny top");

		// =====================================================
		// LIVRO 2
		// =====================================================

		JLabel livro2 = criarLivro("/imagens/livroDemo.png");

		painelLivros.add(livro2, "cell 1 0,alignx center,aligny top");

		// =====================================================
		// LIVRO 3
		// =====================================================

		JLabel livro3 = criarLivro("/imagens/livroDemo.png");

		painelLivros.add(livro3, "cell 2 0,alignx center,aligny top");

		// =====================================================
		// LIVRO 4
		// =====================================================

		JLabel livro4 = criarLivro("/imagens/livroDemo.png");

		painelLivros.add(livro4, "cell 3 0,alignx center,aligny top");

		// =====================================================
		// LIVRO 5
		// =====================================================

		JLabel livro5 = criarLivro("/imagens/livroDemo.png");

		painelLivros.add(livro5, "cell 4 0,alignx center,aligny top");

		// =====================================================
		// LIVRO 6
		// =====================================================

		JLabel livro6 = criarLivro("/imagens/livroDemo.png");

		painelLivros.add(livro6, "cell 5 0,alignx center,aligny top");

		ImageIcon cadastrarIcon = new ImageIcon(TelaMeusLivros.class.getResource("/imagens/BotaoCadastrar.png"));

		Image imagemCadastrar = cadastrarIcon.getImage().getScaledInstance(250, 100, Image.SCALE_SMOOTH);

		JButton btnNewButton = new JButton("");

		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false);

		btnNewButton.setIcon(new ImageIcon(imagemCadastrar));

		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		painelLivros.add(btnNewButton, "cell 0 1 6 1,alignx center,aligny center");

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/Logo.png")));
		contentPane.add(lblNewLabel, "cell 1 0 3 1,alignx center");
	}

	// =====================================================
	// MÉTODO PARA CRIAR A CAPA DO LIVRO
	// =====================================================

	private JLabel criarLivro(String caminho) {

		JLabel livro = new JLabel();

		ImageIcon icone = new ImageIcon(TelaMeusLivros.class.getResource(caminho));

		Image imagem = icone.getImage().getScaledInstance(135, 170, Image.SCALE_SMOOTH);

		livro.setIcon(new ImageIcon(imagem));

		return livro;
	}

	public JButton getBtnHome() {
		return btnHome;
	}

	public JButton getBtnPerfil() {
		return btnPerfil;
	}

	public JButton getBtnMostrarMais() {
		return btnMostrarMais;
	}

}