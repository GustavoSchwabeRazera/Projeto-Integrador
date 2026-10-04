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
import java.awt.Image;

public class JanelaEditar extends JFrame {

    private JPanel contentPane;
    private JTextField txtNome;
    private JTextField txtEditora;
    private JTextField txtAno;
    private JTextField txtAutor;
    private JComboBox comboBox;
    private JButton btnSalvar;

    public JanelaEditar() {

        setIconImage(Toolkit.getDefaultToolkit().getImage(
            JanelaEditar.class.getResource("/imagens/Logo.png")
        ));

        setTitle("Editar Livro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 568, 413);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));

        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout("", "[][grow]", "[][][][][][][89.00,grow]")
        );

        Font fonteLabels = new Font("Tahoma", Font.BOLD, 14);

        // Nome
        JLabel lblNome = new JLabel("Nome:");
        lblNome.setFont(fonteLabels);
        contentPane.add(lblNome, "cell 0 0 1 2,alignx trailing");

        txtNome = new JTextField();
        contentPane.add(txtNome, "cell 1 1,growx");


        // Editora
        JLabel lblEditora = new JLabel("Editora:");
        lblEditora.setFont(fonteLabels);
        contentPane.add(lblEditora, "cell 0 2,alignx trailing");

        txtEditora = new JTextField();
        contentPane.add(txtEditora, "cell 1 2,growx");


        // Ano
        JLabel lblAno = new JLabel("Ano:");
        lblAno.setFont(fonteLabels);
        contentPane.add(lblAno, "cell 0 3,alignx trailing");

        txtAno = new JTextField();
        contentPane.add(txtAno, "cell 1 3,growx");


        // Autor
        JLabel lblAutor = new JLabel("Autor:");
        lblAutor.setFont(fonteLabels);
        contentPane.add(lblAutor, "cell 0 4,alignx trailing");

        txtAutor = new JTextField();
        contentPane.add(txtAutor, "cell 1 4,growx");


        // Gênero
        JLabel lblGenero = new JLabel("Gênero:");
        lblGenero.setFont(fonteLabels);
        contentPane.add(lblGenero, "cell 0 5,alignx trailing");

        comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(Generos.values()));
        contentPane.add(comboBox, "cell 1 5,growx");


        // Botão Salvar
        btnSalvar = new JButton("");
        btnSalvar.setContentAreaFilled(false);
        btnSalvar.setBorderPainted(false);

        // Carrega a imagem original
        ImageIcon iconeOriginal = new ImageIcon(
            JanelaEditar.class.getResource("/imagens/SalvarAlteracao.png")
        );

        // Diminui somente a imagem
        Image imagem = iconeOriginal.getImage().getScaledInstance(
            175, 75, Image.SCALE_SMOOTH
        );

        btnSalvar.setIcon(new ImageIcon(imagem));

        btnSalvar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });

        btnSalvar.setFont(new Font("Tahoma", Font.BOLD, 14));

        contentPane.add(
            btnSalvar,
            "cell 0 6 2 1,alignx center,aligny bottom"
        );
    }


    public JTextField getTxtNome() {
        return txtNome;
    }

    public JTextField getTxtEditora() {
        return txtEditora;
    }

    public JTextField getTxtAno() {
        return txtAno;
    }

    public JTextField getTxtAutor() {
        return txtAutor;
    }

    public JComboBox getComboBox() {
        return comboBox;
    }

    public JButton getBtnSalvar() {
        return btnSalvar;
    }
}