package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Cursor;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import dao.LivroDAO;

import model.Autor;
import model.Livro;

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
	private JButton btnMostrarMais;
	private JButton btnNewButton;

	// Painel onde ficam as capas dos livros
	private JPanel painelCapas;

	// Livros que estão aparecendo na tela
	private List<Livro> livrosNaTela;

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
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));

		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));

		btnHome.setForeground(new Color(10, 86, 27));

		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setFocusPainted(false);

		contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

		ImageIcon perfil = new ImageIcon(TelaMeusLivros.class.getResource("/imagens/perfil3.png"));

		Image imgPerfil = perfil.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

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
		// CAPAS DOS LIVROS (preenchidas em mostrarLivros)
		// =====================================================

		painelCapas = new JPanel(new MigLayout("wrap 6", "[grow,center][grow,center][grow,center][grow,center][grow,center][grow,center]"));
		painelCapas.setOpaque(false);

		JScrollPane scroll = new JScrollPane(painelCapas);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		painelLivros.add(scroll, "cell 0 0 6 1,grow");

		ImageIcon cadastrarIcon = new ImageIcon(
		        TelaMeusLivros.class.getResource("/imagens/BotaoCadastrar.png")
		);

		Image imagemCadastrar = cadastrarIcon.getImage()
		        .getScaledInstance(250, 100, Image.SCALE_SMOOTH);

		btnNewButton = new JButton("");

		btnNewButton.setContentAreaFilled(false);
		btnNewButton.setBorderPainted(false);
		btnNewButton.setFocusPainted(false);

		btnNewButton.setIcon(new ImageIcon(imagemCadastrar));

		painelLivros.add(
		        btnNewButton,
		        "cell 0 1 6 1,alignx center,aligny center"
		);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/Logo.png")));
		contentPane.add(lblNewLabel, "cell 1 0 3 1,alignx center");
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}

	public void setBtnNewButton(JButton btnNewButton) {
		this.btnNewButton = btnNewButton;
	}

	// =====================================================
	// MOSTRAR OS LIVROS DO USUÁRIO
	// =====================================================

	public void mostrarLivros(List<Livro> livros) {

		livrosNaTela = livros;

		painelCapas.removeAll();

		if (livros == null || livros.isEmpty()) {

			JLabel vazio = new JLabel("Você ainda não cadastrou nenhum livro.");
			vazio.setForeground(Color.WHITE);
			vazio.setFont(new Font("Tahoma", Font.BOLD, 20));
			painelCapas.add(vazio, "span 6,alignx center");

		} else {

			for (Livro livro : livros) {
				painelCapas.add(criarLivro(livro), "aligny top");
			}
		}

		painelCapas.revalidate();
		painelCapas.repaint();
	}

	// =====================================================
	// CRIA O BOTÃO COM A CAPA E O TÍTULO
	// =====================================================

	private JButton criarLivro(Livro livro) {

		JButton botao = new JButton(livro.getNome());

		botao.setIcon(new ImageIcon(pegarImagem(livro, 135, 170)));

		// Título embaixo da capa
		botao.setVerticalTextPosition(SwingConstants.BOTTOM);
		botao.setHorizontalTextPosition(SwingConstants.CENTER);
		botao.setForeground(Color.WHITE);
		botao.setFont(new Font("Tahoma", Font.BOLD, 14));

		botao.setContentAreaFilled(false);
		botao.setBorderPainted(false);
		botao.setFocusPainted(false);
		botao.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Ao clicar, mostra as informações
		botao.addActionListener(e -> mostrarInfo(livro));

		return botao;
	}

	// =====================================================
	// MOSTRA AS INFORMAÇÕES DO LIVRO
	// =====================================================

	private void mostrarInfo(Livro livro) {

		String autores = "";

		if (livro.getAutores() != null) {
			for (Autor a : livro.getAutores()) {
				if (!autores.isEmpty()) {
					autores += ", ";
				}
				autores += a.getNome();
			}
		}

		String texto =
				"Título: " + livro.getNome() + "\n"
				+ "Autor(es): " + autores + "\n"
				+ "Editora: " + livro.getEditora() + "\n"
				+ "Ano de lançamento: " + livro.getAnoLancamento() + "\n"
				+ "Gênero: " + livro.getGenero() + "\n"
				+ "ISBN: " + livro.getIsbn();

		// Botões da janela
		String[] opcoes = { "Remover livro", "Fechar" };

		ligarVerde();

		int escolha = JOptionPane.showOptionDialog(
				this,
				texto,
				livro.getNome(),
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE,
				new ImageIcon(pegarImagem(livro, 200, 260)),
				opcoes,
				opcoes[1]
		);

		desligarVerde();

		// 0 = clicou em "Remover livro"
		if (escolha == 0) {
			removerLivro(livro);
		}
	}

	// =====================================================
	// REMOVE O LIVRO
	// =====================================================

	private void removerLivro(Livro livro) {

		ligarVerde();

		int resposta = JOptionPane.showConfirmDialog(
				this,
				"Tem certeza que quer remover \"" + livro.getNome() + "\"?",
				"Remover livro",
				JOptionPane.YES_NO_OPTION
		);

		desligarVerde();

		if (resposta != JOptionPane.YES_OPTION) {
			return;
		}

		try {

			new LivroDAO(null).excluir(livro.getIsbn());

			// Tira o livro da tela
			livrosNaTela.remove(livro);
			mostrarLivros(livrosNaTela);

			mensagem("Livro removido com sucesso!");

		} catch (Exception e) {

			e.printStackTrace();

			// Livro que já foi emprestado não pode ser apagado
			if (e.getMessage() != null && e.getMessage().contains("foreign key")) {
				mensagem("Esse livro tem empréstimos registrados e não pode ser removido.");
			} else {
				mensagem("Erro ao remover o livro:\n" + e.getMessage());
			}
		}
	}

	// =====================================================
	// JANELAS VERDES (IGUAL AO RESTO DO PROJETO)
	// =====================================================

	private void ligarVerde() {
		UIManager.put("OptionPane.background", new Color(175, 244, 198));
		UIManager.put("Panel.background", new Color(175, 244, 198));
	}

	private void desligarVerde() {
		UIManager.put("OptionPane.background", null);
		UIManager.put("Panel.background", null);
	}

	private void mensagem(String texto) {
		ligarVerde();
		JOptionPane.showMessageDialog(this, texto);
		desligarVerde();
	}

	// =====================================================
	// PEGA A FOTO DO LIVRO (OU A IMAGEM PADRÃO)
	// =====================================================

	private Image pegarImagem(Livro livro, int largura, int altura) {

		Image imagem = null;

		// Tenta ler a foto que veio do banco
		if (livro.getFotoCapa() != null && livro.getFotoCapa().length > 0) {
			try {
				BufferedImage foto = ImageIO.read(new ByteArrayInputStream(livro.getFotoCapa()));
				imagem = foto; // fica null se a foto estiver estragada
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Sem foto (ou foto estragada): usa a imagem padrão
		if (imagem == null) {
			imagem = new ImageIcon(TelaMeusLivros.class.getResource("/imagens/livroDemo.png")).getImage();
		}

		return imagem.getScaledInstance(largura, altura, Image.SCALE_SMOOTH);
	}

	public JButton getBtnHome() {
		return btnHome;
	}

	public JButton getBtnMostrarMais() {
		return btnMostrarMais;
	}

}