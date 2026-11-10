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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Autor;
import net.miginfocom.swing.MigLayout;

public class Cadastro_Autor extends JFrame {

private static final long serialVersionUID = 1L;

private JPanel contentPane;

private JTextField txtNome;

private JComboBox<String> comboBox;

private JButton botaoCadastrar;

private JLabel titulo;



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


public Cadastro_Autor() {

    setBackground(
            Cores.FUNDO
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



    contentPane =
            new JPanel();

    contentPane.setBackground(
            Cores.FUNDO
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



    titulo =
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
}

public void prepararEdicao(Autor autor) {

    titulo.setText(
            "Alterar Autor"
    );

    txtNome.setText(
            autor.getNome()
    );

    comboBox.removeItem(
            autor.getNacionalidade()
    );

    comboBox.insertItemAt(
            autor.getNacionalidade(),
            0
    );

    comboBox.setSelectedIndex(0);
}


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
