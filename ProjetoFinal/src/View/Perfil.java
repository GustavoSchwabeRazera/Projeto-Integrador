package View;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Perfil extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnHome;
	private JButton btnAlterarCadastro;

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
	 * Botão com cantos arredondados (usado no botão "Alterar cadastro").
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
	 * Label que desenha a própria imagem já recortada em cantos arredondados
	 * (sem nenhum fundo por trás, então não sobra "branco" nas quinas).
	 */
	private static class RoundedImageLabel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image image;
		private final int radius;

		public RoundedImageLabel(Image image, int radius) {
			this.image = image;
			this.radius = radius;
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
			g2.setClip(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
			g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
			g2.dispose();
		}
	}

	/**
	 * Carrega um ícone a partir do classpath já redimensionado proporcionalmente
	 * (escala menor que 1.0 deixa o ícone menor que o original).
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
	 * Create the frame.
	 */
	public Perfil() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 1300, 721);
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

		// Título "Perfil" - Centralizado no Topo
		JLabel lblPerfil = new JLabel("Perfil");
		lblPerfil.setForeground(new Color(19, 74, 38));
		lblPerfil.setFont(new Font("Tahoma", Font.BOLD, 36));
		contentPane.add(lblPerfil, "cell 2 1, alignx center, aligny center");

		// Texto "Foto de perfil:" acima da foto
		JLabel lblFotoPerfilText = new JLabel("Foto de perfil:");
		lblFotoPerfilText.setForeground(new Color(19, 74, 38));
		lblFotoPerfilText.setFont(new Font("Tahoma", Font.BOLD, 22));
		contentPane.add(lblFotoPerfilText, "cell 1 2, alignx center, aligny bottom");

		// Espaço da foto do usuário (sem imagem por enquanto) — mantém o formato arredondado pronto para quando você adicionar a foto
		RoundedImageLabel lblFoto = new RoundedImageLabel(null, 25);
		contentPane.add(lblFoto, "cell 1 3, alignx center, aligny center, grow");
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Perfil.class.getResource("/imagens/Rectangle 7.png")));
		lblFoto.add(lblNewLabel);

		// Retângulo Verde Escuro de Fundo (Rectangle 7) contendo as informações - agora com cantos arredondados
		RoundedPanel panelDados = new RoundedPanel(
				new MigLayout("", "[20px][580px][20px]", "[20px][50px][50px][50px][50px][20px]"), 30);
		panelDados.setBackground(new Color(25, 90, 45)); // Verde escuro idêntico ao do bloco

		JLabel lblNomeUser = new JLabel("Nome de usuário:   Gustavo S. Razera");
		lblNomeUser.setForeground(Color.WHITE);
		lblNomeUser.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblNomeUser, "cell 1 1, alignx left");

		JLabel lblEmailUser = new JLabel("E-Mail:   gugarazera@gmail.com");
		lblEmailUser.setForeground(Color.WHITE);
		lblEmailUser.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblEmailUser, "cell 1 2, alignx left");

		JLabel lblSenhaUser = new JLabel("Senha:   f*****35");
		lblSenhaUser.setForeground(Color.WHITE);
		lblSenhaUser.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblSenhaUser, "cell 1 3, alignx left");

		JLabel lblLocalizacaoUser = new JLabel("Localização:   Gaspar, SC");
		lblLocalizacaoUser.setForeground(Color.WHITE);
		lblLocalizacaoUser.setFont(new Font("Tahoma", Font.BOLD, 18));
		panelDados.add(lblLocalizacaoUser, "cell 1 4, alignx left");

		contentPane.add(panelDados, "cell 2 3, alignx center, aligny center");

		// Botão "Alterar cadastro" centralizado abaixo dos blocos principais - agora com cantos arredondados
		btnAlterarCadastro = new RoundedButton("Alterar cadastro", 40);
		btnAlterarCadastro.setBackground(new Color(114, 219, 145));
		btnAlterarCadastro.setForeground(Color.BLACK);
		btnAlterarCadastro.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAlterarCadastro.setMargin(new java.awt.Insets(10, 30, 10, 30));
		contentPane.add(btnAlterarCadastro, "cell 2 5, alignx center, aligny center");

		// Painel lateral para alinhar o Calendário e o Sino empilhados no canto inferior direito
		JPanel panelLateralDireita = new JPanel();
		panelLateralDireita.setOpaque(false);
		panelLateralDireita.setLayout(new MigLayout("", "[64px]", "[64px][15px][64px]"));

		// Ícone do Calendário
		JLabel lblCalendario = new JLabel("");
		lblCalendario.setIcon(carregarIconeRedimensionado("/imagens/calendario.png", 0.78));
		panelLateralDireita.add(lblCalendario, "cell 0 0, alignx center");

		// Ícone do Sino (Notificações)
		JLabel lblSino = new JLabel("");
		lblSino.setIcon(carregarIconeRedimensionado("/imagens/sino.png", 0.78));
		panelLateralDireita.add(lblSino, "cell 0 2, alignx center");

		contentPane.add(panelLateralDireita, "cell 3 3 1 4, alignx right, aligny bottom");
	}

    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnAlterarCadastro() {
        return btnAlterarCadastro;
    }

}