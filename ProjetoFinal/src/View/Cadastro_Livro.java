
package View;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class Cadastro_Livro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtEditora;
	private JTextField txtAno;
	private JTextField txtAutor;
	
	
	private JComboBox comboBox;
	private JButton botaoCadastrar;
	

	/**
	 * Launch the application.
	 */
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

	/**
	 * Create the frame.
	 */
	public Cadastro_Livro() {
	    setBackground(new Color(128, 255, 0));
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setSize(1920, 1080);
	    setLocationRelativeTo(null);

	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(175, 244, 198));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(new MigLayout("", "[98.00][86.00][150.00,grow][51.00][118.00]", "[][][][][grow][][][][]"));

	    JLabel lblNewLabel = new JLabel("");

	    ImageIcon logoOriginal = new ImageIcon(
	            Cadastro_Livro.class.getResource("/imagens/Logo.png"));

	    Image logoRedimensionada = logoOriginal.getImage().getScaledInstance(
	            300,
	            150,
	            Image.SCALE_SMOOTH);

	    lblNewLabel.setIcon(new ImageIcon(logoRedimensionada));

	    contentPane.add(lblNewLabel, "cell 0 0 1 2");
	    
	    JLabel lblNewLabel_2 = new JLabel("Cadastro de Livros:");
	    lblNewLabel_2.setForeground(new Color(10, 86, 27));
	    lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 48));
	    contentPane.add(lblNewLabel_2, "cell 2 1,alignx center,aligny bottom");
	    
	    JPanel panel = new ImagePanel();
	    panel.setOpaque(false);
	    contentPane.add(panel, "cell 2 4,grow");
	    panel.setLayout(new MigLayout("", "[234.00][10.00,grow][733.00,grow,center][grow][83.00][165.00]", "[73.00][][][28.00][][][][24.00][][][][][27.00][][][][31.00][35.00][][31.00][][][][grow]"));
	    
	    JLabel lblNewLabel_1 = new JLabel("Nome do Livro");
	    lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 30));
	    panel.add(lblNewLabel_1, "cell 0 1 6 1,alignx center");
	    
	    txtNome = new JTextField();
	    txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    panel.add(txtNome, "cell 2 2,growx,h 42!");
	    txtNome.setColumns(10);
	    
	    JLabel lblNewLabel_1_1_1 = new JLabel("Editora");
	    lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 30));
	    panel.add(lblNewLabel_1_1_1, "cell 0 4 6 1,growx");
	    
	    txtEditora = new JTextField();
	    txtEditora.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    txtEditora.setColumns(10);
	    panel.add(txtEditora, "cell 2 6,growx,h 42!");
	    
	    JLabel lblNewLabel_1_1_1_1 = new JLabel("Ano de Lançamento");
	    lblNewLabel_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 30));
	    panel.add(lblNewLabel_1_1_1_1, "cell 0 9 6 1,alignx center");
	    
	    txtAno = new JTextField();
	    txtAno.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    txtAno.setColumns(10);
	    panel.add(txtAno, "cell 2 11,growx,h 42!");
	    
	    JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Autor");
	    lblNewLabel_1_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 30));
	    panel.add(lblNewLabel_1_1_1_1_1, "cell 0 13 6 1,growx");
	    
	    txtAutor = new JTextField();
	    txtAutor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
	    txtAutor.setColumns(10);
	    panel.add(txtAutor, "cell 2 15,growx,h 42!");
	    
	    JLabel lblNewLabel_1_1_1_1_1_1 = new JLabel("Genero");
	    lblNewLabel_1_1_1_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
	    lblNewLabel_1_1_1_1_1_1.setFont(new Font("Tahoma", Font.BOLD, 30));
	    panel.add(lblNewLabel_1_1_1_1_1_1, "cell 0 17 6 1,alignx center");
	    
	    JPanel panel_1 = new JPanel();
	    panel_1.setVisible(false);
	    
	    
	    comboBox = new JComboBox();
	    comboBox.setModel(new DefaultComboBoxModel(Generos.values()));
	    panel.add(comboBox, "cell 2 18,growx,height 42!");
	    
	    
	    botaoCadastrar = new JButton("");
	    botaoCadastrar.setContentAreaFilled(false);
	    botaoCadastrar.setBorderPainted(false);
	    botaoCadastrar.setIcon(new ImageIcon(Cadastro_Livro.class.getResource("/imagens/BotaoCerto.png")));
	    panel.add(botaoCadastrar, "cell 2 20");
	    panel.add(panel_1, "cell 1 23,grow");
	    
	    JPanel panel_2 = new JPanel();
	    panel_2.setVisible(false);
	    panel.add(panel_2, "cell 2 23");
	    
	    JPanel panel_3 = new JPanel();
	    panel_3.setVisible(false);
	    panel.add(panel_3, "cell 3 23,grow");
	}

	
	public JTextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(JTextField txtNome) {
		this.txtNome = txtNome;
	}

	public JTextField getTxtEditora() {
		return txtEditora;
	}

	public void setTxtEditora(JTextField txtEditora) {
		this.txtEditora = txtEditora;
	}

	public JTextField getTxtAno() {
		return txtAno;
	}

	public void setTxtAno(JTextField txtAno) {
		this.txtAno = txtAno;
	}

	public JTextField getTxtAutor() {
		return txtAutor;
	}

	public void setTxtAutor(JTextField txtAutor) {
		this.txtAutor = txtAutor;
	}

	public JComboBox getComboBox() {
        return this.comboBox;
    }

    public void setComboBox(JComboBox comboBox) {
        this.comboBox = comboBox;
    }

    
    public JButton getBtnAdicionar() {
        return botaoCadastrar;
    }

    
}

