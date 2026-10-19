package View;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Rectangle;
import javax.swing.Scrollable;
import java.awt.event.ComponentAdapter;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;
import java.awt.event.ComponentEvent;
import java.awt.LayoutManager;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import model.RegistroHistorico;

public class Historico extends JFrame {

	private static final long serialVersionUID = 1L;

	private static final Color VERDE_ESCURO = new Color(10, 86, 27);
	private static final Color VERDE_CLARO = new Color(175, 244, 198);
	private static final DateTimeFormatter FORMATO_DATA =
			DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm");

	private JPanel contentPane;
	private JButton btnHome;
	private JPanel listaHistorico;
	private JScrollPane scroll;
	private List<RegistroHistorico> registros = new ArrayList<>();
	private int larguraAnterior = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Historico frame = new Historico();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static class RoundedPanel extends JPanel {
	    private static final long serialVersionUID = 1L;
	    private final int radius;

	    public RoundedPanel(LayoutManager layout, int radius) {
	        super(layout);
	        this.radius = radius;
	        setOpaque(false); // essencial: deixa o fundo "transparente" pro Swing
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

	// Lista que acompanha a largura da área visível (o texto quebra em vez de sair do cartão)
	private static class ListaLarguraFixa extends JPanel implements Scrollable {
	    private static final long serialVersionUID = 1L;

	    public ListaLarguraFixa(LayoutManager layout) {
	        super(layout);
	    }

	    public Dimension getPreferredScrollableViewportSize() {
	        return getPreferredSize();
	    }

	    public int getScrollableUnitIncrement(Rectangle visivel, int orientacao, int direcao) {
	        return 16;
	    }

	    public int getScrollableBlockIncrement(Rectangle visivel, int orientacao, int direcao) {
	        return visivel.height;
	    }

	    public boolean getScrollableTracksViewportWidth() {
	        return true;
	    }

	    public boolean getScrollableTracksViewportHeight() {
	        return false;
	    }
	}

	/**
	 * Create the frame.
	 */
	public Historico() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1920, 1080);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		contentPane = new JPanel();
		contentPane.setForeground(VERDE_ESCURO);
		contentPane.setBackground(VERDE_CLARO);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[307.00,grow][300.00,grow][grow][338.00,grow][322.00,grow]", "[][][grow][grow][grow]"));

		 btnHome = new JButton("");
		 btnHome.setFocusable(false);
		 btnHome.setFocusTraversalKeysEnabled(false);
		btnHome.setDefaultCapable(false);
		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setIcon(new ImageIcon(Historico.class.getResource("/imagens/casa 1.png")));
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(Historico.class.getResource("/imagens/LogoPequena.png")));
		contentPane.add(lblNewLabel, "cell 2 0,alignx center");

		JLabel lblNewLabel_1 = new JLabel("Histórico de Empréstimos");
		lblNewLabel_1.setForeground(VERDE_ESCURO);
		lblNewLabel_1.setBackground(VERDE_ESCURO);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 32));
		contentPane.add(lblNewLabel_1, "cell 2 1,alignx center,aligny center");


		// =====================================================
		// LISTA DO HISTÓRICO
		// =====================================================

		RoundedPanel panel = new RoundedPanel(new MigLayout("insets 0", "[grow]", "[grow]"), 30); // 30 = raio das bordas
		panel.setBackground(VERDE_ESCURO);
		panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
		contentPane.add(panel, "cell 1 2 3 3,grow");

		// Cartões um embaixo do outro (com barra de rolagem quando tiver muitos)
		listaHistorico = new ListaLarguraFixa(new MigLayout("insets 0, wrap 1", "[grow]", "[]12[]"));
		listaHistorico.setOpaque(false);

		scroll = new JScrollPane(listaHistorico);
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		scroll.setBorder(null);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setUnitIncrement(16);

		panel.add(scroll, "cell 0 0,grow");

		// Quando a largura muda, monta os cartões de novo para o texto quebrar certo
		scroll.getViewport().addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				int largura = scroll.getViewport().getWidth();
				if (largura != larguraAnterior) {
					larguraAnterior = largura;
					mostrarHistorico(registros);
				}
			}
		});

		mostrarHistorico(new ArrayList<RegistroHistorico>());
	}


	// =====================================================
	// MOSTRA O HISTÓRICO NA TELA
	// =====================================================

	public void mostrarHistorico(List<RegistroHistorico> registros) {

		this.registros = registros;

		listaHistorico.removeAll();

		if (registros.isEmpty()) {

			JLabel lblVazio = new JLabel("Nenhum empréstimo no histórico.");
			lblVazio.setForeground(Color.WHITE);
			lblVazio.setFont(new Font("Tahoma", Font.BOLD, 22));
			listaHistorico.add(lblVazio, "alignx center,gaptop 40");

		} else {

			for (RegistroHistorico r : registros) {
				listaHistorico.add(criarCartao(r), "growx");
			}
		}

		listaHistorico.revalidate();
		listaHistorico.repaint();
	}


	// =====================================================
	// CARTÃO DE UM REGISTRO
	// =====================================================

	private JPanel criarCartao(RegistroHistorico r) {

		RoundedPanel cartao = new RoundedPanel(
				new MigLayout("insets 12 20 12 20", "[150!]20[grow]", "[]2[]"), 30);
		cartao.setBackground(Color.WHITE);

		// Etiqueta colorida com o tipo da ação
		RoundedPanel etiqueta = new RoundedPanel(new MigLayout("insets 6 10 6 10", "[grow,center]", "[]"), 20);
		etiqueta.setBackground(corDoTipo(r.getTipo()));

		JLabel lblTipo = new JLabel(nomeDoTipo(r.getTipo()), SwingConstants.CENTER);
		lblTipo.setForeground(Color.WHITE);
		lblTipo.setFont(new Font("Tahoma", Font.BOLD, 14));
		etiqueta.add(lblTipo, "growx");

		cartao.add(etiqueta, "cell 0 0 1 2,growx,aligny center");

		// Descrição e data (direto no cartão para a altura acompanhar a quebra de linha)

		// Texto em HTML para quebrar de linha quando for longo
		// (fonte antes do texto, para o HTML já ser montado com ela)
		JLabel lblDescricao = new JLabel();
		lblDescricao.setForeground(VERDE_ESCURO);
		lblDescricao.setFont(new Font("Tahoma", Font.BOLD, 19));
		lblDescricao.setVerticalAlignment(SwingConstants.TOP);
		lblDescricao.setText("<html>" + escaparHtml(r.getDescricao()) + "</html>");
		ajustarLargura(lblDescricao, larguraDoTexto());
		cartao.add(lblDescricao, "cell 1 0");

		JLabel lblDetalhes = new JLabel(r.getDataHora().format(FORMATO_DATA));
		lblDetalhes.setForeground(new Color(60, 60, 60));
		lblDetalhes.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cartao.add(lblDetalhes, "cell 1 1");

		return cartao;
	}

	// Espaço que sobra no cartão para o texto (tira etiqueta, margens e espaços)
	private int larguraDoTexto() {

		int largura = (scroll != null) ? scroll.getViewport().getWidth() : 0;

		if (largura <= 0) {
			return 600;
		}

		return Math.max(200, largura - 260);
	}

	// Calcula a altura que o texto precisa nessa largura (quantas linhas vai ocupar)
	private void ajustarLargura(JLabel label, int largura) {

		View view = (View) label.getClientProperty(BasicHTML.propertyKey);

		if (view == null) {
			return;
		}

		view.setSize(largura, 0);

		int altura = (int) Math.ceil(view.getPreferredSpan(View.Y_AXIS));

		label.setPreferredSize(new Dimension(largura, altura));
	}

	private String escaparHtml(String texto) {
		return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
	}

	private String nomeDoTipo(String tipo) {

		switch (tipo) {
			case RegistroHistorico.EMPRESTIMO_CONCEDIDO:
				return "Emprestei";
			case RegistroHistorico.EMPRESTIMO_RECEBIDO:
				return "Peguei";
			default:
				return tipo;
		}
	}

	private Color corDoTipo(String tipo) {

		switch (tipo) {
			case RegistroHistorico.EMPRESTIMO_CONCEDIDO:
				return new Color(200, 120, 0);
			case RegistroHistorico.EMPRESTIMO_RECEBIDO:
				return new Color(30, 100, 160);
			default:
				return Color.GRAY;
		}
	}

	public JButton getBtnHome() {
		return btnHome;
	}

	public void setBtnHome(JButton btnHome) {
		this.btnHome = btnHome;
	}


}
