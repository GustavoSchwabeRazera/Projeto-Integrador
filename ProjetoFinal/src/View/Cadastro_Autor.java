package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
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

        Graphics2D g2 =
                (Graphics2D) g.create();

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(
                new Color(10, 86, 27)
        );

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


// Cadastro de autor
public class Cadastro_Autor extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTextField txtNome;

    private JComboBox<String> comboBox;

    private JButton botaoCadastrar;

    // Tela de livros que abriu o cadastro
    private Cadastro_Livro cadastroLivro;


    public static void main(String[] args) {

        EventQueue.invokeLater(
                new Runnable() {

                    public void run() {

                        try {

                            Cadastro_Autor frame =
                                    new Cadastro_Autor();

                            frame.setVisible(true);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }
                }
        );
    }


    // Construtor padrão
    public Cadastro_Autor() {

        this(null);
    }


    // Construtor recebendo a tela de livros
    public Cadastro_Autor(
            Cadastro_Livro cadastroLivro) {

        this.cadastroLivro =
                cadastroLivro;

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

        JLabel lblNewLabel =
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

        lblNewLabel.setIcon(
                new ImageIcon(
                        logoRedimensionada
                )
        );

        contentPane.add(
                lblNewLabel,
                "cell 0 0 1 2"
        );


        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel lblNewLabel_2 =
                new JLabel(
                        "Cadastro de Autores"
                );

        lblNewLabel_2.setForeground(
                new Color(10, 86, 27)
        );

        lblNewLabel_2.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        48
                )
        );

        contentPane.add(
                lblNewLabel_2,
                "cell 2 1,alignx center,aligny bottom"
        );


        // =====================================================
        // PAINEL PRINCIPAL
        // =====================================================

        JPanel panel =
                new ImagePanel();

        panel.setForeground(
                Color.WHITE
        );

        panel.setBackground(
                new Color(10, 86, 27)
        );

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

        JLabel lblTitulo =
                new JLabel(
                        "NOME COMPLETO DO AUTOR:"
                );

        lblTitulo.setForeground(
                new Color(10, 86, 27)
        );

        lblTitulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        lblTitulo.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        30
                )
        );

        panel.add(
                lblTitulo,
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
        // COMBOBOX DE PAÍSES
        // =====================================================

        comboBox =
                new JComboBox<String>();

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

        botaoCadastrar.setContentAreaFilled(
                false
        );

        botaoCadastrar.setBorderPainted(
                false
        );

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


        // Evento do botão
        botaoCadastrar.addActionListener(
                e -> cadastrarAutor()
        );


        // =====================================================
        // PAINÉIS OCULTOS
        // =====================================================

        JPanel panel_1 =
                new JPanel();

        panel_1.setVisible(false);

        panel.add(
                panel_1,
                "cell 1 26,grow"
        );


        JPanel panel_2 =
                new JPanel();

        panel_2.setVisible(false);

        panel.add(
                panel_2,
                "cell 2 26"
        );


        JPanel panel_3 =
                new JPanel();

        panel_3.setVisible(false);

        panel.add(
                panel_3,
                "cell 3 26,grow"
        );
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


        // Validação do nome
        if (nome.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Digite o nome do autor."
            );

            txtNome.requestFocus();

            return;
        }


        // Validação da nacionalidade
        if (nacionalidade.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione a nacionalidade."
            );

            return;
        }


        try {

            // Cria o objeto Autor
            Autor autor =
                    new Autor(
                            0,
                            nome,
                            nacionalidade
                    );


            // Cria o DAO
            AutorDAO autorDAO =
                    new AutorDAO(
                            ConnectionFactory.getConnection()
                    );


            // Salva no banco
            autorDAO.cadastrar(
                    autor
            );


            // =================================================
            // ATUALIZA A LISTA DA TELA DE LIVROS
            // =================================================

            if (cadastroLivro != null) {

                cadastroLivro.carregarAutores(
                        autorDAO.listarAutores()
                );
            }


            // Mensagem
            JOptionPane.showMessageDialog(
                    this,
                    "Autor cadastrado com sucesso!"
            );


            // Limpa o campo
            txtNome.setText("");


            // Fecha a janela
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
    // GETTERS E SETTERS
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


    public void setCadastroLivro(
            Cadastro_Livro cadastroLivro) {

        this.cadastroLivro =
                cadastroLivro;
    }


    public Cadastro_Livro getCadastroLivro() {

        return cadastroLivro;
    }
}
