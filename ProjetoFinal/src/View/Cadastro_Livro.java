package View;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;

import net.miginfocom.swing.MigLayout;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.Image;

import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


// Botão arredondado

class RoundedButton extends JButton {

	
	private static final long serialVersionUID = 1L;

	public RoundedButton(String texto) {

		super(texto);

		setForeground(Color.WHITE);
		setBackground(new Color(10, 86, 27));
		setFont(new Font("Segoe UI", Font.BOLD, 16));

		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setOpaque(false);
	}

	@Override
	protected void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		);

		g2.setColor(new Color(10, 86, 27));

		g2.fillRoundRect(
				0,
				0,
				getWidth(),
				getHeight(),
				20,
				20
		);

		g2.dispose();

		super.paintComponent(g);
	}
}


// Cadastro de livro
public class Cadastro_Livro extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	private JTextField txtNome;
	private JTextField txtEditora;
	private JTextField txtAno;

	private JComboBox comboBox;

	private JButton botaoCadastrar;
	private JTextField textField;

	private JList listaAutores;

	private JButton botaoNovoAutor;
	private JButton botaoCadastrarAutor;


	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				try {

					Cadastro_Livro frame = new Cadastro_Livro();

					frame.setVisible(true);

				} catch (Exception e) {

					e.printStackTrace();

				}
			}
		});
	}


	public Cadastro_Livro() {

		setBackground(new Color(128, 255, 0));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		setSize(1920, 1080);

		setLocationRelativeTo(null);


		contentPane = new JPanel();

		contentPane.setBackground(
				new Color(175, 244, 198)
		);

		contentPane.setBorder(
				new EmptyBorder(5, 5, 5, 5)
		);

		setContentPane(contentPane);


		contentPane.setLayout(new MigLayout(
				"",
				"[98.00][86.00][150.00,grow][51.00][118.00]",
				"[][][][][grow][][][][]"
		));


		// Logo
		JLabel lblNewLabel = new JLabel("");

		ImageIcon logoOriginal = new ImageIcon(
				Cadastro_Livro.class.getResource(
						"/imagens/Logo.png"
				)
		);

		Image logoRedimensionada =
				logoOriginal.getImage().getScaledInstance(
						300,
						150,
						Image.SCALE_SMOOTH
				);

		lblNewLabel.setIcon(
				new ImageIcon(logoRedimensionada)
		);

		contentPane.add(
				lblNewLabel,
				"cell 0 0 1 2"
		);


		// Título
		JLabel lblNewLabel_2 =
				new JLabel("Cadastro de Livros:");

		lblNewLabel_2.setForeground(
				new Color(10, 86, 27)
		);

		lblNewLabel_2.setFont(
				new Font("Tahoma", Font.BOLD, 48)
		);

		contentPane.add(
				lblNewLabel_2,
				"cell 2 1,alignx center,aligny bottom"
		);


		// Painel principal
		JPanel panel = new ImagePanel();

		panel.setForeground(
				new Color(255, 255, 255)
		);

		panel.setBackground(
				new Color(10, 86, 27)
		);

		panel.setOpaque(false);

		contentPane.add(
				panel,
				"cell 2 4,grow"
		);


		panel.setLayout(new MigLayout(
				"",
				"[234.00][10.00,grow][733.00,grow,center][grow][83.00][165.00]",
				"[73.00][][][28.00][][][][24.00][][][][grow][27.00][][][][31.00][35.00][][31.00][][][][][][grow]"
		));


		// Título do livro
		JLabel lblTitulo =
				new JLabel("TÍTULO DO LIVRO:");

		lblTitulo.setForeground(
				new Color(10, 86, 27)
		);

		lblTitulo.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		lblTitulo.setFont(
				new Font("Tahoma", Font.BOLD, 30)
		);

		panel.add(
				lblTitulo,
				"cell 0 1 6 1,alignx center"
		);


		txtNome = new JTextField();

		txtNome.setFont(
				new Font("Segoe UI", Font.PLAIN, 16)
		);

		txtNome.setColumns(10);

		txtNome.setBorder(
				BorderFactory.createLineBorder(
						new Color(10, 86, 27),
						2
				)
		);

		panel.add(
				txtNome,
				"cell 2 2,growx,h 42!"
		);


		// Editora
		JLabel lblEditora =
				new JLabel("EDITORA:");

		lblEditora.setForeground(
				new Color(10, 86, 27)
		);

		lblEditora.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		lblEditora.setFont(
				new Font("Tahoma", Font.BOLD, 30)
		);

		panel.add(
				lblEditora,
				"cell 0 4 6 1,growx"
		);


		txtEditora = new JTextField();

		txtEditora.setFont(
				new Font("Segoe UI", Font.PLAIN, 16)
		);

		txtEditora.setColumns(10);

		txtEditora.setBorder(
				BorderFactory.createLineBorder(
						new Color(10, 86, 27),
						2
				)
		);

		panel.add(
				txtEditora,
				"cell 2 5,growx,height 42!"
		);


		// Data de lançamento
		JLabel lblData =
				new JLabel("DATA DE LANÇAMENTO:");

		lblData.setForeground(
				new Color(10, 86, 27)
		);

		lblData.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		lblData.setFont(
				new Font("Tahoma", Font.BOLD, 30)
		);

		panel.add(
				lblData,
				"cell 2 7,alignx center"
		);


		txtAno = new JTextField();

		txtAno.setFont(
				new Font("Segoe UI", Font.PLAIN, 16)
		);

		txtAno.setColumns(10);

		txtAno.setBorder(
				BorderFactory.createLineBorder(
						new Color(10, 86, 27),
						2
				)
		);

		panel.add(
				txtAno,
				"cell 2 8,growx,height 42!"
		);


		// Autor
		JLabel lblAutor =
				new JLabel("AUTOR:");

		lblAutor.setForeground(
				new Color(10, 86, 27)
		);

		lblAutor.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		lblAutor.setFont(
				new Font("Tahoma", Font.BOLD, 30)
		);

		panel.add(
				lblAutor,
				"cell 2 10,growx"
		);


		// Área dos autores
		JPanel painelAutores =
				new JPanel();

		painelAutores.setOpaque(false);

		painelAutores.setLayout(
				new MigLayout(
						"",
						"[grow]",
						"[][45!]"
				)
		);


		// Lista de autores
		JPanel painelListaAutores =
				new JPanel();

		painelListaAutores.setBackground(
				Color.WHITE
		);

		painelListaAutores.setBorder(
				BorderFactory.createLineBorder(
						new Color(10, 86, 27),
						2
				)
		);

		painelListaAutores.setLayout(
				new MigLayout(
						"",
						"[grow]",
						"[grow][]"
				)
		);


		listaAutores =
				new JList();

		listaAutores.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						18
				)
		);

		listaAutores.setBorder(null);

		painelListaAutores.add(
				listaAutores,
				"cell 0 0,grow"
		);


		// Cadastrar novo autor
		botaoCadastrarAutor =
				new RoundedButton(
						"Cadastrar novo autor"
				);

		painelListaAutores.add(
				botaoCadastrarAutor,
				"cell 0 1,growx,height 40!"
		);

		botaoCadastrarAutor.addActionListener(
				new ActionListener() {

					public void actionPerformed(
							ActionEvent e
					) {

						// Abrir tela de cadastro de autor

					}
				}
		);


		painelAutores.add(
				painelListaAutores,
				"cell 0 0,growx,height 140!"
		);


		// Selecionar autor(es)
		botaoNovoAutor =
				new RoundedButton(
						"Selecionar autor(es)"
				);

		painelAutores.add(
				botaoNovoAutor,
				"cell 0 1,growx,height 45!"
		);

		botaoNovoAutor.addActionListener(
				new ActionListener() {

					public void actionPerformed(
							ActionEvent e
					) {

						// Selecionar autor(es)

					}
				}
		);


		panel.add(
				painelAutores,
				"cell 2 11,growx"
		);


		// Gênero
		JLabel lblGenero =
				new JLabel("GÊNERO:");

		lblGenero.setForeground(
				new Color(10, 86, 27)
		);

		lblGenero.setHorizontalAlignment(
				SwingConstants.CENTER
		);

		lblGenero.setFont(
				new Font("Tahoma", Font.BOLD, 30)
		);

		panel.add(
				lblGenero,
				"cell 2 13,alignx center"
		);


		comboBox =
				new JComboBox();

		comboBox.setFont(
				new Font("Tahoma", Font.BOLD, 11)
		);

		comboBox.setForeground(
				new Color(10, 89, 27)
		);

		comboBox.setBackground(
				new Color(255, 255, 255)
		);

		comboBox.setBorder(
				BorderFactory.createLineBorder(
						new Color(10, 86, 27),
						2
				)
		);

		comboBox.setModel(
				new DefaultComboBoxModel(
						Generos.values()
				)
		);

		panel.add(
				comboBox,
				"cell 2 14,growx,height 42!"
		);


		// ISBN
		JLabel lblISBN =
				new JLabel("ISBN:");

		lblISBN.setForeground(
				new Color(10, 86, 27)
		);

		lblISBN.setFont(
				new Font("Tahoma", Font.BOLD, 30)
		);

		panel.add(
				lblISBN,
				"cell 2 16"
		);


		textField =
				new JTextField();

		textField.setFont(
				new Font(
						"Segoe UI",
						Font.PLAIN,
						16
				)
		);

		textField.setColumns(10);

		textField.setBorder(
				BorderFactory.createLineBorder(
						new Color(10, 86, 27),
						2
				)
		);

		panel.add(
				textField,
				"cell 2 17,growx,height 42!"
		);


		// Botão cadastrar
		botaoCadastrar =
				new JButton("");

		botaoCadastrar.setContentAreaFilled(
				false
		);

		botaoCadastrar.setBorderPainted(
				false
		);

		botaoCadastrar.setIcon(
				new ImageIcon(
						Cadastro_Livro.class.getResource(
								"/imagens/BotaoCerto.png"
						)
				)
		);

		panel.add(
				botaoCadastrar,
				"cell 2 23"
		);


		// Painéis ocultos
		JPanel panel_1 =
				new JPanel();

		panel_1.setVisible(false);

		panel.add(
				panel_1,
				"cell 1 25,grow"
		);


		JPanel panel_2 =
				new JPanel();

		panel_2.setVisible(false);

		panel.add(
				panel_2,
				"cell 2 25"
		);


		JPanel panel_3 =
				new JPanel();

		panel_3.setVisible(false);

		panel.add(
				panel_3,
				"cell 3 25,grow"
		);

	}


	// Getters e setters

	public JTextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(
			JTextField txtNome
	) {
		this.txtNome = txtNome;
	}


	public JTextField getTxtEditora() {
		return txtEditora;
	}

	public void setTxtEditora(
			JTextField txtEditora
	) {
		this.txtEditora = txtEditora;
	}


	public JTextField getTxtAno() {
		return txtAno;
	}

	public void setTxtAno(
			JTextField txtAno
	) {
		this.txtAno = txtAno;
	}


	public JComboBox getComboBox() {
		return this.comboBox;
	}

	public void setComboBox(
			JComboBox comboBox
	) {
		this.comboBox = comboBox;
	}


	public JButton getBtnAdicionar() {
		return botaoCadastrar;
	}


	public JList getListaAutores() {
		return listaAutores;
	}


	public JButton getBotaoNovoAutor() {
		return botaoNovoAutor;
	}


	public JButton getBotaoCadastrarAutor() {
		return botaoCadastrarAutor;
	}

}
