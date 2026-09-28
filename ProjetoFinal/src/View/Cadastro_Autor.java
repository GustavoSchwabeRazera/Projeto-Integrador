package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.AutorDAO;
import dao.ConnectionFactory;
import model.Autor;
import net.miginfocom.swing.MigLayout;

public class Cadastro_Autor extends JFrame {

private static final long serialVersionUID = 1L;

private JPanel contentPane;

private JTextField txtNome;

private JComboBox<String> comboBox;

private JButton botaoCadastrar;

// Lista de autores que abriu esta tela
private Lista_Autores listaAutores;


// =========================================================
// MAIN
// =========================================================

public static void main(String[] args) {

    EventQueue.invokeLater(() -> {

        try {

            Cadastro_Autor frame =
                    new Cadastro_Autor();

            frame.setVisible(true);

        } catch (Exception e) {

            e.printStackTrace();
        }
    });
}




// =========================================================
// CONSTRUTOR PADRÃO
// =========================================================

public Cadastro_Autor() {

    setBackground(
            new Color(128, 255, 0)
    );

    setDefaultCloseOperation(
            JFrame.DISPOSE_ON_CLOSE
    );

    setExtendedState(
            JFrame.MAXIMIZED_BOTH
    );

    setSize(
            1920,
            1080
    );

    setLocationRelativeTo(null);


    // =====================================================
    // PAINEL
    // =====================================================

    contentPane =
            new JPanel();

    contentPane.setBackground(
            new Color(175, 244, 198)
    );

    contentPane.setBorder(
            new EmptyBorder(
                    5,
                    5,
                    5,
                    5
            )
    );

    setContentPane(
            contentPane
    );

    contentPane.setLayout(
            new MigLayout(
                    "",
                    "[98.00][86.00][150.00,grow][240.00][118.00]",
                    "[][][][][grow][][][][]"
            )
    );


    // =====================================================
    // LOGO
    // =====================================================

    JLabel logo =
            new JLabel("");

    ImageIcon logoOriginal =
            new ImageIcon(
                    Cadastro_Autor.class.getResource(
                            "/imagens/Logo.png"
                    )
            );

    Image logoRedimensionada =
            logoOriginal.getImage()
                    .getScaledInstance(
                            300,
                            150,
                            Image.SCALE_SMOOTH
                    );

    logo.setIcon(
            new ImageIcon(
                    logoRedimensionada
            )
    );

    contentPane.add(
            logo,
            "cell 0 0 1 2"
    );


    // =====================================================
    // TÍTULO
    // =====================================================

    JLabel titulo =
            new JLabel(
                    "Cadastro de Autores"
            );

    titulo.setForeground(
            new Color(10, 86, 27)
    );

    titulo.setFont(
            new Font(
                    "Tahoma",
                    Font.BOLD,
                    48
            )
    );

    contentPane.add(
            titulo,
            "cell 2 1,alignx center,aligny bottom"
    );


    // =====================================================
    // PAINEL
    // =====================================================

    JPanel panel =
            new ImagePanel();

    panel.setOpaque(false);

    contentPane.add(
            panel,
            "cell 2 4,grow"
    );

    panel.setLayout(
            new MigLayout(
                    "",
                    "[234.00][10.00,grow][733.00,grow,center][grow][83.00][165.00]",
                    "[73.00][][][28.00][][][][24.00][][][][grow][][27.00][][][][31.00][35.00][][31.00][][][][][][grow]"
            )
    );


    // =====================================================
    // NOME
    // =====================================================

    JLabel lblNome =
            new JLabel(
                    "NOME COMPLETO DO AUTOR:"
            );

    lblNome.setForeground(
            new Color(10, 86, 27)
    );

    lblNome.setHorizontalAlignment(
            SwingConstants.CENTER
    );

    lblNome.setFont(
            new Font(
                    "Tahoma",
                    Font.BOLD,
                    30
            )
    );

    panel.add(
            lblNome,
            "cell 0 1 6 1,alignx center"
    );


    txtNome =
            new JTextField();

    txtNome.setFont(
            new Font(
                    "Segoe UI",
                    Font.PLAIN,
                    16
            )
    );

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


    // =====================================================
    // NACIONALIDADE
    // =====================================================

    JLabel lblNacionalidade =
            new JLabel(
                    "NACIONALIDADE:"
            );

    lblNacionalidade.setForeground(
            new Color(10, 86, 27)
    );

    lblNacionalidade.setHorizontalAlignment(
            SwingConstants.CENTER
    );

    lblNacionalidade.setFont(
            new Font(
                    "Tahoma",
                    Font.BOLD,
                    30
            )
    );

    panel.add(
            lblNacionalidade,
            "cell 0 4 6 1,growx"
    );


    // =====================================================
    // PAÍSES
    // =====================================================

    comboBox =
            new JComboBox<>();

    String[] codigosPaises =
            Locale.getISOCountries();

    ArrayList<String> paises =
            new ArrayList<>();

    for (String codigo :
            codigosPaises) {

        Locale pais =
                new Locale(
                        "",
                        codigo
                );

        String nomePais =
                pais.getDisplayCountry(
                        new Locale(
                                "pt",
                                "BR"
                        )
                );

        paises.add(
                nomePais
        );
    }

    Collections.sort(
            paises
    );

    for (String pais :
            paises) {

        comboBox.addItem(
                pais
        );
    }

    panel.add(
            comboBox,
            "cell 2 5,growx,h 42!"
    );


    // =====================================================
    // BOTÃO CADASTRAR
    // =====================================================

    botaoCadastrar =
            new JButton("");

    botaoCadastrar.setContentAreaFilled(false);

    botaoCadastrar.setBorderPainted(false);

    botaoCadastrar.setIcon(
            new ImageIcon(
                    Cadastro_Autor.class.getResource(
                            "/imagens/BotaoCerto.png"
                    )
            )
    );

    panel.add(
            botaoCadastrar,
            "cell 2 12"
    );


    botaoCadastrar.addActionListener(
            e -> cadastrarAutor()
    );
}


// =========================================================
// CONSTRUTOR RECEBENDO LISTA
// =========================================================

public Cadastro_Autor(
        Lista_Autores listaAutores) {

    this();

    this.listaAutores =
            listaAutores;
}


// =========================================================
// CADASTRAR AUTOR
// =========================================================

private void cadastrarAutor() {

    String nome =
            txtNome.getText().trim();


    String nacionalidade = "";


    if (comboBox.getSelectedItem() != null) {

        nacionalidade =
                comboBox.getSelectedItem()
                        .toString()
                        .trim();
    }


    // =====================================================
    // VALIDAÇÕES
    // =====================================================

    if (nome.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Digite o nome do autor."
        );

        txtNome.requestFocus();

        return;
    }


    if (nacionalidade.isEmpty()) {

        JOptionPane.showMessageDialog(
                this,
                "Selecione a nacionalidade."
        );

        return;
    }


    // =====================================================
    // SALVAR NO BANCO
    // =====================================================

    try {

        Autor autor =
                new Autor(
                        0,
                        nome,
                        nacionalidade
                );


        AutorDAO autorDAO =
                new AutorDAO(
                        ConnectionFactory.getConnection()
                );


        autorDAO.cadastrar(
                autor
        );


        JOptionPane.showMessageDialog(
                this,
                "Autor cadastrado com sucesso!"
        );


        // =================================================
        // ATUALIZAR LISTA DE AUTORES
        // =================================================

        if (listaAutores != null) {

            listaAutores.recarregarAutores();
        }


        // Fecha cadastro do autor
        dispose();


    } catch (Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Erro ao cadastrar o autor:\n"
                        + ex.getMessage(),
                "Erro",
                JOptionPane.ERROR_MESSAGE
        );
    }
}


// =========================================================
// GETTERS
// =========================================================

public JTextField getTxtNome() {

    return txtNome;
}


public void setTxtNome(
        JTextField txtNome) {

    this.txtNome =
            txtNome;
}


public JComboBox<String> getComboBox() {

    return comboBox;
}


public void setComboBox(
        JComboBox<String> comboBox) {

    this.comboBox =
            comboBox;
}


public JButton getBtnAdicionar() {

    return botaoCadastrar;
}

}