package View;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class PesquisarLivro extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JButton btnHome;
    private JButton btnPesquisar;
    private JButton btnPerfil;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PesquisarLivro frame = new PesquisarLivro();
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
    public PesquisarLivro() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1920, 1080);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout(
                "",
                "[][grow][][][1070.00,grow][51.00,grow]",
                "[][][35.00,grow][105.00,grow][104.00,grow][][][grow]"
            )
        );


        // =====================================================
        // LOGO
        // =====================================================

        JLabel lblNewLabel_3 = new JLabel("");

        ImageIcon logo = new ImageIcon(
            PesquisarLivro.class.getResource("/imagens/Logo.png")
        );

        Image imagemRedimensionada = logo.getImage()
            .getScaledInstance(350, 190, Image.SCALE_SMOOTH);


        // =====================================================
        // BOTÃO PERFIL
        // =====================================================

        btnPerfil = new JButton("");

        // TAMANHO DO BOTÃO PERFIL
        btnPerfil.setPreferredSize(
            new Dimension(70, 70)
        );

        btnPerfil.setBorderPainted(false);
        btnPerfil.setContentAreaFilled(false);
        btnPerfil.setFocusPainted(false);


        // TAMANHO DA IMAGEM DO PERFIL
        ImageIcon perfil = new ImageIcon(
            PesquisarLivro.class.getResource(
                "/imagens/FotoPerfil.png"
            )
        );

        Image imagemPerfilRedimensionada = perfil.getImage()
            .getScaledInstance(
                80,
                80,
                Image.SCALE_SMOOTH
            );

        btnPerfil.setIcon(
            new ImageIcon(imagemPerfilRedimensionada)
        );


        btnPerfil.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            }
        );

        contentPane.add(
            btnPerfil,
            "cell 5 0,alignx right"
        );


        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel lblNewLabel = new JLabel("Pesquisar Livros");

        lblNewLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        lblNewLabel.setFont(
            new Font("Tahoma", Font.BOLD, 51)
        );

        contentPane.add(
            lblNewLabel,
            "cell 0 1 6 1,alignx center"
        );


        // =====================================================
        // LOGO CENTRAL
        // =====================================================

        lblNewLabel_3.setIcon(
            new ImageIcon(imagemRedimensionada)
        );

        contentPane.add(
            lblNewLabel_3,
            "flowy,cell 0 3 6 1,alignx center"
        );


        // =====================================================
        // BOTÃO HOME
        // =====================================================

        btnHome = new JButton("");

        // TAMANHO DO BOTÃO HOME
        btnHome.setPreferredSize(
            new Dimension(80, 80)
        );

        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);


        // TAMANHO DA IMAGEM DO HOME
        ImageIcon casa = new ImageIcon(
            PesquisarLivro.class.getResource(
                "/imagens/LogoCasa.png"
            )
        );

        Image imagemCasaRedimensionada = casa.getImage()
            .getScaledInstance(
                70,
                70,
                Image.SCALE_SMOOTH
            );

        btnHome.setIcon(
            new ImageIcon(imagemCasaRedimensionada)
        );


        btnHome.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            }
        );

        contentPane.add(
            btnHome,
            "cell 0 0 2 1,aligny top"
        );


        // =====================================================
        // LOCALIZAÇÃO
        // =====================================================

        JButton btnNewButton_1 = new JButton("Gaspar");

        btnNewButton_1.setFont(
            new Font("Tahoma", Font.BOLD, 36)
        );

        btnNewButton_1.setBorderPainted(false);
        btnNewButton_1.setContentAreaFilled(false);

        btnNewButton_1.setIcon(
            new ImageIcon(
                PesquisarLivro.class.getResource(
                    "/imagens/LogoLocalizacao.png"
                )
            )
        );

        btnNewButton_1.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                }
            }
        );

        contentPane.add(
            btnNewButton_1,
            "cell 0 7 3 1,alignx left,aligny bottom"
        );


        // =====================================================
        // CAMPO DE PESQUISA
        // =====================================================

        textField = new JTextField();

        textField.setFont(
            new Font("Segoe UI", Font.PLAIN, 16)
        );

        contentPane.add(
            textField,
            "cell 4 4 1 2,growx,height 42!"
        );

        textField.setColumns(10);


        // =====================================================
        // BOTÃO PESQUISAR
        // =====================================================

        btnPesquisar = new JButton("");

        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setContentAreaFilled(false);

        ImageIcon pesquisar = new ImageIcon(
            PesquisarLivro.class.getResource(
                "/imagens/pesquisar.png"
            )
        );

        Image imagemPesquisarRedimensionada = pesquisar.getImage()
            .getScaledInstance(
                200,
                75,
                Image.SCALE_SMOOTH
            );

        btnPesquisar.setIcon(
            new ImageIcon(imagemPesquisarRedimensionada)
        );

        contentPane.add(
            btnPesquisar,
            "cell 1 6 5 1,alignx center"
        );
    }

    public JButton getBtnHome() {
        return btnHome;
    }

    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }

    public JButton getBtnPerfil() {
        return btnPerfil;
    }

    public String getTextoPesquisa() {
        return textField.getText().trim();
    }

    public void limparPesquisa() {
        textField.setText("");
    }

}