package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.miginfocom.swing.MigLayout;
import java.awt.Toolkit;

public class Perfil extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnHome;
	private JButton btnAlterarCadastro;
	private JButton btnAlterarFoto; 
	private JLabel lblNomeUser;
	private JLabel lblEmailUser;
	private JLabel lblTelefone;
	private JLabel lblDataDeNascimento;
	private CircularImageLabel lblFoto;
	private byte[] fotoSelecionada;
	private String cpfUsuario;
	private JButton btnNewButton;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Perfil frame = new Perfil();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Painel com cantos arredondados (usado no bloco verde escuro de dados).
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
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(getBackground());
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
			g2.dispose();
			super.paintComponent(g);
		}
	}

	/**
	 * Botão com cantos arredondados.
	 */
	private static class RoundedButton extends JButton {
		private static final long serialVersionUID = 1L;
		private final int radius;
		private boolean hovering = false;

		public RoundedButton(String text, int radius) {
			super(text);
			this.radius = radius;
			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setOpaque(false);
			setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

			addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseEntered(java.awt.event.MouseEvent e) {
					hovering = true;
					repaint();
				}

				@Override
				public void mouseExited(java.awt.event.MouseEvent e) {
					hovering = false;
					repaint();
				}
			});
		}

		private Color corAtual() {
			Color base = getBackground();
			if (getModel().isPressed()) {
				return base.darker();
			}
			if (hovering) {
				return new Color(
						Math.max((int) (base.getRed() * 0.9), 0),
						Math.max((int) (base.getGreen() * 0.9), 0),
						Math.max((int) (base.getBlue() * 0.9), 0));
			}
			return base;
		}

		@Override
		protected void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setColor(corAtual());
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
			g2.dispose();
			super.paintComponent(g);
		}
	}

	/**
	 * Componente que exibe a imagem em formato circular (círculo perfeito).
	 */
	private static class CircularImageLabel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image image;

		public CircularImageLabel(Image image) {
			this.image = image;
			setOpaque(false);
		}

		public void setImage(Image image) {
			this.image = image;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (image == null) {
				return;
			}
			Graphics2D g2 = (Graphics2D) g.create();
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

			int size = Math.min(getWidth(), getHeight());
			int xOffset = (getWidth() - size) / 2;
			int yOffset = (getHeight() - size) / 2;

			// Define o corte circular
			g2.setClip(new Ellipse2D.Float(xOffset, yOffset, size, size));

			int imgWidth = image.getWidth(this);
			int imgHeight = image.getHeight(this);

			if (imgWidth > 0 && imgHeight > 0) {
				// Calcula a escala para cobrir o círculo sem distorcer
				double scaleWidth = (double) size / imgWidth;
				double scaleHeight = (double) size / imgHeight;
				double scale = Math.max(scaleWidth, scaleHeight);

				int drawWidth = (int) (imgWidth * scale);
				int drawHeight = (int) (imgHeight * scale);

				int x = xOffset + (size - drawWidth) / 2;
				int y = yOffset + (size - drawHeight) / 2;

				g2.drawImage(image, x, y, drawWidth, drawHeight, this);
			}

			g2.dispose();
		}
	}

	/**
	 * Carrega um ícone a partir do classpath redimensionado proporcionalmente.
	 */
	private static ImageIcon carregarIconeRedimensionado(String caminho, double escala) {
		java.net.URL url = Perfil.class.getResource(caminho);
		if (url == null) {
			System.err.println("Aviso: não encontrei o ícone " + caminho + " no classpath.");
			return null;
		}
		ImageIcon original = new ImageIcon(url);
		int novaLargura = Math.max((int) Math.round(original.getIconWidth() * escala), 1);
		int novaAltura = Math.max((int) Math.round(original.getIconHeight() * escala), 1);
		Image imagemRedimensionada = original.getImage().getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);
		return new ImageIcon(imagemRedimensionada);
	}

	/**
	 * Ação para selecionar e carregar a foto do usuário através do JFileChooser.
	 */
	
	public void selecionarEAtualizarFoto() {

	    JFileChooser fileChooser = new JFileChooser();

	    FileNameExtensionFilter filtro =
	            new FileNameExtensionFilter(
	                    "Imagens (*.png, *.jpg, *.jpeg, *.gif)",
	                    "png", "jpg", "jpeg", "gif"
	            );

	    fileChooser.setFileFilter(filtro);

	    int resultado = fileChooser.showOpenDialog(this);

	    if (resultado == JFileChooser.APPROVE_OPTION) {

	        File arquivoSelecionado = fileChooser.getSelectedFile();

	        try {

	            // Converte a imagem para byte[]
	            fotoSelecionada =
	                    java.nio.file.Files.readAllBytes(
	                            arquivoSelecionado.toPath()
	                    );

	            // Mostra a imagem imediatamente na tela
	            ImageIcon imagem =
	                    new ImageIcon(arquivoSelecionado.getAbsolutePath());

	            lblFoto.setImage(imagem.getImage());

	            lblFoto.revalidate();
	            lblFoto.repaint();

	        } catch (Exception e) {

	            e.printStackTrace();

	            JOptionPane.showMessageDialog(
	                    this,
	                    "Erro ao carregar a imagem."
	            );
	        }
	    }
	}


	/**
	 * Create the frame.
	 */
	public Perfil() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 1300, 721);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(175, 244, 198));
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[30px][350px,grow][650px,grow][30px]", "[30px][50px][40px][380px][40px][60px][30px,grow]"));

		// Ícone da Casa (Home) - Canto Superior Esquerdo
		btnHome = new JButton("");
		btnHome.setIcon(carregarIconeRedimensionado("/imagens/casa 2.png", 0.78));
		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setFocusPainted(false);
		btnHome.setOpaque(false);
		contentPane.add(btnHome, "cell 1 1, alignx left, aligny center");
		
		btnNewButton = new JButton("");
		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setIcon(new ImageIcon(Perfil.class.getResource("/imagens/sairAjustado.png")));
		contentPane.add(btnNewButton, "cell 3 1");

		// Título "Informações" - Formatado igual ao "Foto de perfil:"
		JLabel lblPerfil = new JLabel("Informações:");
		lblPerfil.setForeground(new Color(19, 74, 38));
		lblPerfil.setFont(new Font("Tahoma", Font.BOLD, 22));
		contentPane.add(lblPerfil, "cell 2 2, alignx center, aligny bottom");

		// Texto "Foto de perfil:" acima da foto
		JLabel lblFotoPerfilText = new JLabel("Foto de perfil:");
		lblFotoPerfilText.setForeground(new Color(19, 74, 38));
		lblFotoPerfilText.setFont(new Font("Tahoma", Font.BOLD, 22));
		contentPane.add(lblFotoPerfilText, "cell 1 2, alignx center, aligny bottom");

		// Espaço da foto em CÍRCULO com tamanho quadrado 280x280px
		lblFoto = new CircularImageLabel(null);
		lblFoto.setImage(Toolkit.getDefaultToolkit().getImage(Perfil.class.getResource("/imagens/perfil3.png")));
		contentPane.add(lblFoto, "cell 1 3, width 280!, height 280!, alignx center, aligny center");

		// Retângulo Verde Escuro de Fundo contendo as informações
		RoundedPanel panelDados = new RoundedPanel(
				new MigLayout("", "[20px][580px][20px]", "[20px][50px][50px][50px][50px][20px]"), 30);
		panelDados.setBackground(new Color(25, 90, 45));

		lblNomeUser = new JLabel("Nome de usuário:");
		lblNomeUser.setForeground(Color.WHITE);
		lblNomeUser.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblNomeUser, "cell 1 1, alignx left");

		lblEmailUser = new JLabel("E-Mail:");
		lblEmailUser.setForeground(Color.WHITE);
		lblEmailUser.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblEmailUser, "cell 1 2, alignx left");

		lblTelefone = new JLabel("Telefone:");
		lblTelefone.setForeground(Color.WHITE);
		lblTelefone.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblTelefone, "cell 1 3, alignx left");

		lblDataDeNascimento = new JLabel("Data de Nascimento:");
		lblDataDeNascimento.setForeground(Color.WHITE);
		lblDataDeNascimento.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblDataDeNascimento, "cell 1 4, alignx left");

		contentPane.add(panelDados, "cell 2 3, alignx center, aligny center");

		// Botão "Alterar foto"
		btnAlterarFoto = new RoundedButton("Alterar foto", 40);
		btnAlterarFoto.setBackground(new Color(114, 219, 145));
		btnAlterarFoto.setForeground(Color.BLACK);
		btnAlterarFoto.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAlterarFoto.setMargin(new java.awt.Insets(10, 30, 10, 30));
		
			
		contentPane.add(btnAlterarFoto, "cell 1 5, alignx center, aligny center");

		// Botão "Alterar cadastro"
		btnAlterarCadastro = new RoundedButton("Alterar cadastro", 40);
		btnAlterarCadastro.setBackground(new Color(114, 219, 145));
		btnAlterarCadastro.setForeground(Color.BLACK);
		btnAlterarCadastro.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAlterarCadastro.setMargin(new java.awt.Insets(10, 30, 10, 30));
		
		contentPane.add(btnAlterarCadastro, "cell 2 5, alignx center, aligny center");

		// Painel lateral para alinhar Calendário e Sino
		JPanel panelLateralDireita = new JPanel();
		panelLateralDireita.setOpaque(false);
		panelLateralDireita.setLayout(new MigLayout("", "[64px]", "[64px][15px][64px]"));

		JLabel lblCalendario = new JLabel("");
		lblCalendario.setIcon(carregarIconeRedimensionado("/imagens/calendario.png", 0.78));
		panelLateralDireita.add(lblCalendario, "cell 0 0, alignx center");

		JLabel lblSino = new JLabel("");
		lblSino.setIcon(carregarIconeRedimensionado("/imagens/sino.png", 0.78));
		panelLateralDireita.add(lblSino, "cell 0 2, alignx center");

		contentPane.add(panelLateralDireita, "cell 3 3 1 4, alignx right, aligny bottom");
	}

	public void atualizarDados(String nome, String email, String telefone, String dataNascimento) {
		lblNomeUser.setText("Nome de usuário:   " + nome);
		lblEmailUser.setText("E-Mail:   " + email);
		lblTelefone.setText("Telefone:   " + telefone);
		lblDataDeNascimento.setText("Data de Nascimento:   " + dataNascimento);
	}
	public void setCpfUsuario(String cpfUsuario) {
	    this.cpfUsuario = cpfUsuario;
	}

	public byte[] getFotoSelecionada() {
	    return fotoSelecionada;
	}

	public void carregarFoto(byte[] foto) {

	    if (foto != null && foto.length > 0) {

	        ImageIcon imagem = new ImageIcon(foto);

	        lblFoto.setImage(imagem.getImage());

	        lblFoto.revalidate();
	        lblFoto.repaint();
	    }
	}

	public JButton getBtnHome() {
		return btnHome;
	}
	

	public JButton getBtnAlterarCadastro() {
		return btnAlterarCadastro;
	}

	public JButton getBtnAlterarFoto() {
		return btnAlterarFoto;
	}
}