package View;

import java.awt.EventQueue;
import model.UsuarioTableModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import model.Usuario;
import View.TelaUsuario;
import javax.swing.JOptionPane;

public class Cadastro_usuario extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panel;
	private JLabel lblNewLabel_1;
	private JLabel lblNewLabel_3;
	private JTextField txtNome;
	private JTextField txtEmail;
	private JLabel lblNewLabel_4;
	private JTextField txtSenha;
	private JLabel lblNewLabel_5;
	private JTextField txtLoc;
	private JButton botaoEntrar;
	private JButton btnNewButton;
	private JComboBox comboBox;
    private UsuarioTableModel model = new UsuarioTableModel();
    private TelaUsuario tela = new TelaUsuario();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Cadastro frame = new Cadastro();
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
	public Cadastro_usuario() {
		
		
	    setBackground(new Color(128, 255, 0));
	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	    setSize(1920, 1080);
	    setLocationRelativeTo(null);
	    tela.getTabela().setModel(model);

	    contentPane = new JPanel();
	    contentPane.setBackground(new Color(175, 244, 198));
	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	    setContentPane(contentPane);
	    contentPane.setLayout(new MigLayout("", "[98.00][86.00][150.00,grow][51.00][118.00]", "[][][][][grow][][][]"));

	    JLabel lblNewLabel = new JLabel("");
	    lblNewLabel.setIcon(new ImageIcon(Cadastro.class.getResource("/imagens/Logo.png")));
	    contentPane.add(lblNewLabel, "cell 0 0 1 2");
	    
	    JLabel lblNewLabel_2 = new JLabel("Cadastro de Usuário:");
	    lblNewLabel_2.setForeground(new Color(10, 86, 27));
	    lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 48));
	    contentPane.add(lblNewLabel_2, "cell 2 1,alignx center,aligny bottom");
	    
	    panel = new ImagePanel();
	    panel.setOpaque(false);
	    contentPane.add(panel, "cell 2 4,grow");
	    panel.setLayout(new MigLayout("", "[126.00][grow][][][453.00,grow][grow]", "[39.00][][][40.00][][][50.00][][][44.00][][][27.00][][][][11.00][]"));
	    
	    lblNewLabel_1 = new JLabel("NOME:");
	    lblNewLabel_1.setForeground(new Color(10, 86, 27));
	    lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 23));
	    lblNewLabel_1.setHorizontalAlignment(SwingConstants.RIGHT);
	    panel.add(lblNewLabel_1, "cell 0 1 6 1,alignx center");
	    
	    txtNome = new JTextField();
	    panel.add(txtNome, "cell 1 2 4 1,growx");
	    txtNome.setColumns(10);
	    
	    lblNewLabel_3 = new JLabel("E-MAIL:");
	    lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
	    lblNewLabel_3.setForeground(new Color(10, 86, 27));
	    lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 23));
	    panel.add(lblNewLabel_3, "cell 0 4 6 1,alignx center");
	    
	    txtEmail = new JTextField();
	    panel.add(txtEmail, "cell 1 5 4 1,growx");
	    txtEmail.setColumns(10);
	    
	    lblNewLabel_4 = new JLabel("DEFINIR SENHA:");
	    lblNewLabel_4.setForeground(new Color(10, 86, 27));
	    lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 23));
	    panel.add(lblNewLabel_4, "cell 0 7 6 1,alignx center");
	    
	    txtSenha = new JTextField();
	    panel.add(txtSenha, "cell 1 8 4 1,growx");
	    txtSenha.setColumns(10);
	    
	    lblNewLabel_5 = new JLabel("LOCALIZAÇÃO(\"Cidade/Estado\"):");
	    lblNewLabel_5.setForeground(new Color(10, 86, 27));
	    lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 23));
	    panel.add(lblNewLabel_5, "cell 0 10 6 1,alignx center");
	    
	    txtLoc = new JTextField();
	    txtLoc.setColumns(10);
	    panel.add(txtLoc, "cell 1 11 4 1,growx");
	    
	    JLabel lblNewLabel_6 = new JLabel("GÊNERO:");
	    lblNewLabel_6.setForeground(new Color(10, 86, 27));
	    lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 23));
	    panel.add(lblNewLabel_6, "cell 0 13 6 1,alignx center");
	    
	    botaoEntrar = new JButton("");
	    botaoEntrar.setContentAreaFilled(false);
	    botaoEntrar.setBorderPainted(false);
	    botaoEntrar.setIcon(new ImageIcon(Cadastro.class.getResource("/imagens/BotaoEntrar.png")));
	    botaoEntrar.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent e) {
	        }
	    });
	    
	    comboBox = new JComboBox();
	    comboBox.setModel(new DefaultComboBoxModel(genero.values()));
	    panel.add(comboBox, "cell 1 14 4 1,growx");
	    panel.add(botaoEntrar, "cell 0 17 3 1,alignx right");
	    
	    btnNewButton = new JButton("");
	    btnNewButton.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent e) {
	    	}
	    });
	    btnNewButton.setContentAreaFilled(false);
	    btnNewButton.setBorderPainted(false);
	    btnNewButton.setIcon(new ImageIcon(Cadastro.class.getResource("/imagens/Group 10.png")));
	    panel.add(btnNewButton, "cell 3 17 2 1,alignx left");
	    }
	

	public JComboBox getComboBox() {
		return comboBox;
	}

	public void setComboBox(JComboBox comboBox) {
		this.comboBox = comboBox;
	}

	public JTextField getTxtNome() {
		return txtNome;
	}

	public void setTxtNome(JTextField txtNome) {
		this.txtNome = txtNome;
	}

	public JTextField getTxtEmail() {
		return txtEmail;
	}

	public void setTxtEmail(JTextField txtEmail) {
		this.txtEmail = txtEmail;
	}

	public JTextField getTxtSenha() {
		return txtSenha;
	}

	public void setTxtSenha(JTextField txtSenha) {
		this.txtSenha = txtSenha;
	}

	public JTextField getTxtLoc() {
		return txtLoc;
	}

	public void setTxtLoc(JTextField txtLoc) {
		this.txtLoc = txtLoc;
	}
	
	public JButton getBotaoEntrar() {
	    return botaoEntrar;
	}
	
	public UsuarioTableModel getModel() {
	    return model;
	}
	
	public TelaUsuario criarTelaUsuario() {
	    return new TelaUsuario();
	}
	public JButton getBtnMostrarTabela() {
	    return btnNewButton;
	}
}
