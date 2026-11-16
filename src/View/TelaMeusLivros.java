package View;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import model.Autor;
import model.Livro;
import net.miginfocom.swing.MigLayout;

// Lista dos livros do usuário, no mesmo estilo de Livros Comigo e Solicitações:
// painel verde arredondado com um cartão claro para cada livro.
// Em cima da lista ficam a busca e o filtro, para quando houver muitos livros.
public class TelaMeusLivros extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final String FILTRO_TODOS = "Todos";
	private static final String FILTRO_DISPONIVEIS = "Disponíveis";
	private static final String FILTRO_EMPRESTADOS = "Emprestados";

	// Mesmas cores dos cartões de Livros Comigo
	private static final Color VERDE_PAINEL = new Color(36, 107, 45);
	private static final Color VERDE_BOTAO = new Color(114, 219, 145);
	private static final Color TEXTO_SUAVE = new Color(60, 60, 60);
	private static final Color VERMELHO = new Color(140, 20, 20);
	private static final Color LARANJA = new Color(120, 60, 0);

	private JPanel contentPane;
	private JButton btnHome;
	private JButton btnMostrarMais;
	private JButton btnNewButton;

	private JTextField txtBusca;
	private JComboBox<String> comboFiltro;
	private JLabel lblContador;

	// Painel com os cartões da lista
	private JPanel painelLista;

	// Livros do usuário (todos, antes do filtro)
	private List<Livro> livrosNaTela = new ArrayList<>();

	// Com quem está cada livro emprestado (ISBN -> descrição)
	private final Map<String, String> comQuemPorIsbn = new HashMap<>();

	// O LivroController preenche
	private Consumer<Livro> aoAlterar;
	private Consumer<Livro> aoRemover;
	private Consumer<Livro> aoDevolver;
	private Function<Livro, String> buscarComQuem;

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

		EventQueue.invokeLater(() -> {
			try {
				TelaMeusLivros frame = new TelaMeusLivros();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
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

		contentPane = new JPanel();
		contentPane.setBackground(new Color(175, 244, 198));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		// Mesmo layout de Livros Comigo e Solicitações
		contentPane.setLayout(new MigLayout("", "[200,grow][200,grow][200,grow][200,grow][200,grow]",
				"[][][grow][grow][grow][grow][grow][grow][grow][grow]"));

		// BOTÃO HOME
		btnHome = new JButton("");
		btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));
		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
		btnHome.setForeground(Cores.VERDE);
		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setFocusPainted(false);
		btnHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
		contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

		// LOGO
		JLabel lblLogo = new JLabel("");
		lblLogo.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/Logo.png")));
		contentPane.add(lblLogo, "cell 1 0 3 1,alignx center,aligny top");

		// TÍTULO
		JLabel lblMeusLivros = new JLabel("Meus Livros");
		lblMeusLivros.setForeground(Cores.VERDE);
		lblMeusLivros.setFont(new Font("Tahoma", Font.BOLD, 34));
		contentPane.add(lblMeusLivros, "cell 0 1 5 1,alignx center");

		// PAINEL VERDE: barra de busca, lista e botão de cadastrar
		RoundedPanel painelLivros = new RoundedPanel(
				new MigLayout("insets 0, wrap 1", "[grow,fill]", "[]15[grow,fill]15[]"), 40);
		painelLivros.setBackground(VERDE_PAINEL);
		painelLivros.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
		contentPane.add(painelLivros, "cell 0 2 5 8,grow");

		painelLivros.add(criarBarraBusca());

		painelLista = new JPanel(new MigLayout("insets 0, wrap 1", "[grow,fill]", "[]15[]"));
		painelLista.setOpaque(false);

		JScrollPane scroll = new JScrollPane(painelLista);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(16);
		painelLivros.add(scroll);

		// Mesmo botão do Pesquisar
		btnNewButton = new BotaoVerde("Cadastrar livro", 200, 50);
		painelLivros.add(btnNewButton, "alignx center,growx 0,width 200!,height 50!");
	}

	// Busca, filtro e contador, em cima da lista
	private JPanel criarBarraBusca() {

		JPanel barra = new JPanel(new MigLayout("insets 0, gapx 12", "[400!][180!][grow]", "[42!]"));
		barra.setOpaque(false);

		txtBusca = new CampoComDica("Buscar por título, autor ou ISBN");
		txtBusca.setBackground(new Color(255, 255, 255));
		txtBusca.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtBusca.setBorder(new EmptyBorder(0, 12, 0, 12));
		txtBusca.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) { atualizarLista(); }
			public void removeUpdate(DocumentEvent e) { atualizarLista(); }
			public void changedUpdate(DocumentEvent e) { atualizarLista(); }
		});
		barra.add(txtBusca, "grow");

		comboFiltro = new JComboBox<>(new String[] { FILTRO_TODOS, FILTRO_DISPONIVEIS, FILTRO_EMPRESTADOS });
		comboFiltro.setFont(new Font("Tahoma", Font.PLAIN, 15));
		comboFiltro.setBackground(new Color(255, 255, 255));
		comboFiltro.setCursor(new Cursor(Cursor.HAND_CURSOR));
		comboFiltro.addActionListener(e -> atualizarLista());
		barra.add(comboFiltro, "grow");

		lblContador = new JLabel(" ");
		lblContador.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblContador.setForeground(Color.WHITE);
		barra.add(lblContador, "alignx right");

		return barra;
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

		livrosNaTela = livros != null ? livros : new ArrayList<>();

		// Busca uma vez só com quem está cada livro emprestado
		comQuemPorIsbn.clear();
		for (Livro livro : livrosNaTela) {
			if (!livro.isDisponivel() && buscarComQuem != null) {
				String comQuem = buscarComQuem.apply(livro);
				if (comQuem != null) {
					comQuemPorIsbn.put(livro.getIsbn(), comQuem);
				}
			}
		}

		atualizarLista();
	}

	// Monta a lista de novo aplicando a busca e o filtro
	private void atualizarLista() {

		painelLista.removeAll();

		String busca = semAcento(txtBusca.getText().trim());
		String filtro = (String) comboFiltro.getSelectedItem();

		int mostrados = 0;

		for (Livro livro : livrosNaTela) {

			if (FILTRO_DISPONIVEIS.equals(filtro) && !livro.isDisponivel()) {
				continue;
			}
			if (FILTRO_EMPRESTADOS.equals(filtro) && livro.isDisponivel()) {
				continue;
			}
			if (!busca.isEmpty() && !semAcento(textoDeBusca(livro)).contains(busca)) {
				continue;
			}

			painelLista.add(criarCartao(livro), "growx");
			mostrados++;
		}

		// Mensagem igual à de Livros Comigo quando não há nada para mostrar
		if (mostrados == 0) {
			JLabel vazio = new JLabel(livrosNaTela.isEmpty()
					? "Você ainda não cadastrou nenhum livro."
					: "Nenhum livro encontrado com essa busca.");
			vazio.setForeground(Color.WHITE);
			vazio.setFont(new Font("Tahoma", Font.BOLD, 22));
			painelLista.add(vazio, "alignx center,gaptop 40");
		}

		int total = livrosNaTela.size();
		lblContador.setText(total == 0 ? " "
				: mostrados == total ? total + (total == 1 ? " livro" : " livros")
				: mostrados + " de " + total + " livros");

		painelLista.revalidate();
		painelLista.repaint();
	}

	private String textoDeBusca(Livro livro) {
		return livro.getNome() + " " + nomesAutores(livro) + " " + livro.getIsbn()
				+ " " + livro.getEditora() + " " + livro.getGenero();
	}

	// Deixa a busca ignorar acentos e maiúsculas ("memorias" acha "Memórias")
	private static String semAcento(String texto) {
		return Normalizer.normalize(texto == null ? "" : texto, Normalizer.Form.NFD)
				.replaceAll("\\p{M}", "")
				.toLowerCase();
	}

	private static String nomesAutores(Livro livro) {

		StringBuilder nomes = new StringBuilder();

		if (livro.getAutores() != null) {
			for (Autor a : livro.getAutores()) {
				if (nomes.length() > 0) {
					nomes.append(", ");
				}
				nomes.append(a.getNome());
			}
		}

		return nomes.toString();
	}

	// =====================================================
	// CARTÃO DE UM LIVRO (mesmo formato de Livros Comigo)
	// =====================================================

	private JPanel criarCartao(Livro livro) {

		RoundedPanel cartao = new RoundedPanel(
				new MigLayout("insets 14 25 14 25", "[]20[grow]20[]", "[center]"), 35);
		cartao.setBackground(Cores.FUNDO);

		// Capa
		cartao.add(new JLabel(new ImageIcon(pegarImagem(livro, 80, 105))), "cell 0 0,alignx center,aligny center");

		// Título + detalhes + status
		JPanel informacoes = new JPanel(new MigLayout("insets 0", "[grow]", "[]4[]4[]"));
		informacoes.setOpaque(false);
		cartao.add(informacoes, "cell 1 0,growx,aligny center");

		JLabel lblTitulo = new JLabel(livro.getNome());
		lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 21));
		informacoes.add(lblTitulo, "cell 0 0");

		String autores = nomesAutores(livro);
		StringBuilder detalhes = new StringBuilder(autores.isEmpty() ? "Autor não informado" : autores);
		if (livro.getEditora() != null && !livro.getEditora().isBlank()) {
			detalhes.append("   •   ").append(livro.getEditora());
		}
		if (livro.getAnoLancamento() > 0) {
			detalhes.append("   •   ").append(livro.getAnoLancamento());
		}
		if (livro.getGenero() != null && !livro.getGenero().isBlank()) {
			detalhes.append("   •   ").append(livro.getGenero());
		}
		JLabel lblDetalhes = new JLabel(detalhes.toString());
		lblDetalhes.setForeground(TEXTO_SUAVE);
		lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
		informacoes.add(lblDetalhes, "cell 0 1");

		JLabel lblStatus = new JLabel();
		lblStatus.setFont(new Font("Tahoma", Font.BOLD, 15));
		if (livro.isDisponivel()) {
			lblStatus.setText("Disponível");
			lblStatus.setForeground(Cores.VERDE);
		} else {
			String comQuem = comQuemPorIsbn.get(livro.getIsbn());
			lblStatus.setText(comQuem != null ? "Emprestado para " + comQuem : "Emprestado");
			lblStatus.setForeground(LARANJA);
		}
		informacoes.add(lblStatus, "cell 0 2");

		// Botões
		JPanel acoes = new JPanel(new MigLayout("insets 0, gapx 10", "[][]", "[center]"));
		acoes.setOpaque(false);

		JButton btnAlterar = botaoAcao("Alterar", VERDE_BOTAO, Color.BLACK);
		btnAlterar.addActionListener(e -> aoAlterar.accept(livro));
		acoes.add(btnAlterar);

		if (livro.isDisponivel()) {
			JButton btnRemover = botaoAcao("Remover", VERMELHO, Color.WHITE);
			btnRemover.addActionListener(e -> aoRemover.accept(livro));
			acoes.add(btnRemover);
		} else {
			JButton btnDevolucao = botaoAcao("Registrar devolução", VERDE_BOTAO, Color.BLACK);
			btnDevolucao.addActionListener(e -> aoDevolver.accept(livro));
			acoes.add(btnDevolucao);
		}

		cartao.add(acoes, "cell 2 0,alignx center,aligny center");

		// Clicar no cartão (fora dos botões) abre os detalhes
		cartao.setCursor(new Cursor(Cursor.HAND_CURSOR));
		cartao.setToolTipText("Clique para ver os detalhes");
		cartao.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				mostrarInfo(livro);
			}
		});

		return cartao;
	}

	// Mesmo botão do "Devolver" de Livros Comigo
	private JButton botaoAcao(String texto, Color fundo, Color cor) {

		JButton botao = new BotaoArredondado(texto, 40);
		botao.setBackground(fundo);
		botao.setForeground(cor);
		botao.setFont(new Font("Tahoma", Font.BOLD, 16));
		botao.setMargin(new Insets(8, 24, 8, 24));
		return botao;
	}

	// Campo de texto com uma dica cinza enquanto está vazio
	private static class CampoComDica extends JTextField {

		private static final long serialVersionUID = 1L;
		private final String dica;

		CampoComDica(String dica) {
			this.dica = dica;
		}

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);

			if (getText().isEmpty()) {
				Graphics2D g2 = (Graphics2D) g.create();
				g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g2.setColor(new Color(140, 140, 140));
				g2.setFont(getFont());
				int y = (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
				g2.drawString(dica, getInsets().left, y);
				g2.dispose();
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(400, 42);
		}
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

		// Quem está com o livro (null se está disponível)
		String comQuem = null;

		if (!livro.isDisponivel()) {
			comQuem = buscarComQuem.apply(livro);
		}

		String texto =
				"Título: " + livro.getNome() + "\n"
				+ "Autor(es): " + autores + "\n"
				+ "Editora: " + livro.getEditora() + "\n"
				+ "Ano de lançamento: " + livro.getAnoLancamento() + "\n"
				+ "Gênero: " + livro.getGenero() + "\n"
				+ "ISBN: " + livro.getIsbn() + "\n"
				+ "Status: " + (livro.isDisponivel() ? "Disponível" : "Emprestado")
				+ (comQuem != null ? " para " + comQuem : "");

		// Livro emprestado: troca "Remover" por "Registrar devolução"
		String[] opcoes = livro.isDisponivel()
				? new String[] { "Alterar livro", "Remover livro", "Fechar" }
				: new String[] { "Alterar livro", "Registrar devolução", "Fechar" };

		int escolha = JanelaMensagem.escolher(
				texto,
				livro.getNome(),
				new ImageIcon(pegarImagem(livro, 200, 260)),
				opcoes
		);

		if (escolha == 0) {
			aoAlterar.accept(livro);
		} else if (escolha == 1) {
			if (livro.isDisponivel()) {
				aoRemover.accept(livro);
			} else {
				aoDevolver.accept(livro);
			}
		}
	}

	public void setAoAlterar(Consumer<Livro> aoAlterar) {
		this.aoAlterar = aoAlterar;
	}

	public void setAoRemover(Consumer<Livro> aoRemover) {
		this.aoRemover = aoRemover;
	}

	public void setAoDevolver(Consumer<Livro> aoDevolver) {
		this.aoDevolver = aoDevolver;
	}

	public void setBuscarComQuem(Function<Livro, String> buscarComQuem) {
		this.buscarComQuem = buscarComQuem;
	}

	// Botão arredondado (copiado do Perfil)
	static class BotaoArredondado extends JButton {

		private static final long serialVersionUID = 1L;
		private final int radius;
		private boolean hovering = false;

		public BotaoArredondado(String text, int radius) {

			super(text);

			this.radius = radius;

			setContentAreaFilled(false);
			setFocusPainted(false);
			setBorderPainted(false);
			setOpaque(false);
			setCursor(new Cursor(Cursor.HAND_CURSOR));

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
						(int) (base.getRed() * 0.9),
						(int) (base.getGreen() * 0.9),
						(int) (base.getBlue() * 0.9));
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

	// =====================================================
	// PEGA A FOTO DO LIVRO (OU A IMAGEM PADRÃO)
	// =====================================================

	static Image pegarImagem(Livro livro, int largura, int altura) {

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

		// Sem foto (ou foto estragada): mostra uma capa com "?"
		if (imagem == null) {
			return capaSemFoto(largura, altura);
		}

		return cortarParaCaber((BufferedImage) imagem, largura, altura);
	}

	// =====================================================
	// AJUSTA A FOTO AO ESPAÇO SEM DEFORMAR
	// Aumenta ou diminui mantendo a proporção até cobrir o espaço todo
	// e corta o que sobrar dos lados (ou de cima e de baixo), pelo centro.
	// =====================================================

	static Image cortarParaCaber(BufferedImage foto, int largura, int altura) {

		double escala = Math.max(
				(double) largura / foto.getWidth(),
				(double) altura / foto.getHeight());

		int novaLargura = (int) Math.ceil(foto.getWidth() * escala);
		int novaAltura = (int) Math.ceil(foto.getHeight() * escala);

		// getScaledInstance com SCALE_SMOOTH dá um resultado melhor ao diminuir muito a foto
		Image redimensionada = foto.getScaledInstance(novaLargura, novaAltura, Image.SCALE_SMOOTH);

		BufferedImage capa = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = capa.createGraphics();
		g2.drawImage(redimensionada, (largura - novaLargura) / 2, (altura - novaAltura) / 2, null);
		g2.dispose();

		return capa;
	}

	// =====================================================
	// CAPA PADRÃO COM PONTO DE INTERROGAÇÃO
	// =====================================================

	private static Image capaSemFoto(int largura, int altura) {

		BufferedImage capa = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = capa.createGraphics();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// Fundo claro
		g2.setColor(new Color(235, 235, 235));
		g2.fillRect(0, 0, largura, altura);

		// Borda verde
		g2.setColor(new Color(10, 86, 27));
		g2.drawRect(0, 0, largura - 1, altura - 1);

		// "?" grande no meio
		g2.setFont(new Font("Tahoma", Font.BOLD, altura / 2));

		int larguraTexto = g2.getFontMetrics().stringWidth("?");
		int x = (largura - larguraTexto) / 2;
		int y = (altura - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();

		g2.drawString("?", x, y);

		g2.dispose();

		return capa;
	}

	public JButton getBtnHome() {
		return btnHome;
	}

	// Não é mais usado na tela, mas continua aqui para não quebrar quem chama
	public JButton getBtnMostrarMais() {
		return btnMostrarMais;
	}
}
