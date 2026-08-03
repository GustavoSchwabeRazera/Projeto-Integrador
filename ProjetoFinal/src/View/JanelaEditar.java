package View;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Toolkit;

public class JanelaEditar extends JFrame {

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtEditora;
    private JTextField txtAno;
    private JTextField txtAutor;
    private JComboBox comboBox;
    private JButton btnSalvar;

    public JanelaEditar() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(JanelaEditar.class.getResource("/imagens/Logo.png")));
        setTitle("Editar Livro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 568, 413);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(new MigLayout("", "[][grow]", "[][][][][][grow]"));

        Font fonteLabels = new Font("Tahoma", Font.BOLD, 14);

        contentPane.add(new JLabel("Nome:"), "cell 0 0,alignx trailing");
        txtNome = new JTextField();
        contentPane.add(txtNome, "cell 1 0,growx");

        contentPane.add(new JLabel("Editora:"), "cell 0 1,alignx trailing");
        txtEditora = new JTextField();
        contentPane.add(txtEditora, "cell 1 1,growx");

        contentPane.add(new JLabel("Ano:"), "cell 0 2,alignx trailing");
        txtAno = new JTextField();
        contentPane.add(txtAno, "cell 1 2,growx");

        contentPane.add(new JLabel("Autor:"), "cell 0 3,alignx trailing");
        txtAutor = new JTextField();
        contentPane.add(txtAutor, "cell 1 3,growx");

        contentPane.add(new JLabel("Gênero:"), "cell 0 4,alignx trailing");
        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(Generos.values()));
        contentPane.add(comboBox, "cell 1 4,growx");

        btnSalvar = new JButton("");
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorderPainted(false);
        btnSalvar.setIcon(new ImageIcon(JanelaEditar.class.getResource("/imagens/Alteracao.png")));
        btnSalvar.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        	}
        });
        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));
        contentPane.add(btnSalvar, "cell 0 5 2 1,alignx center,aligny bottom");
    }

    
    public JTextField getTxtNome() { return txtNome; }
    public JTextField getTxtEditora() { return txtEditora; }
    public JTextField getTxtAno() { return txtAno; }
    public JTextField getTxtAutor() { return txtAutor; }
    public JComboBox getComboBox() { return comboBox; }
    public JButton getBtnSalvar() { return btnSalvar; }
}