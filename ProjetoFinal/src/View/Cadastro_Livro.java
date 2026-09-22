package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.AutorDAO;
import dao.ConnectionFactory;
import model.Autor;
import net.miginfocom.swing.MigLayout;


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

    private JList<Autor> listaAutores;

    private JButton botaoNovoAutor;

    private JButton botaoCadastrarAutor;


    public static void main(String[] args) {

        EventQueue.invokeLater(
                new Runnable() {

                    public void run() {

                        try {

                            Cadastro_Livro frame =
                                    new Cadastro_Livro();

                            frame.setVisible(true);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }
                    }
                }
        );
    }


    public Cadastro_Livro() {

        setBackground(
                new Color(128, 255, 0)
        );

        setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE
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
                        "[98.00][86.00][150.00,grow][51.00][118.00]",
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
                        Cadastro_Livro.class.getResource(
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
                        "Cadastro de Livros:"
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
                        "[73.00][][][28.00][][][][][][][][grow][27.00][][][][31.00][35.00][][][][grow]"
                )
        );


        // =====================================================
        // TÍTULO DO LIVRO
        // =====================================================

        JLabel lblTitulo =
                new JLabel(
                        "TÍTULO DO LIVRO:"
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
        // EDITORA
        // =====================================================

        JLabel lblEditora =
                new JLabel(
                        "EDITORA:"
                );

        lblEditora.setForeground(
                new Color(10, 86, 27)
        );

        lblEditora.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        lblEditora.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        30
                )
        );

        panel.add(
                lblEditora,
                "cell 0 4 6 1,growx"
        );


        txtEditora =
                new JTextField();

        txtEditora.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
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


        // =====================================================
        // ANO DE LANÇAMENTO
        // =====================================================

        JLabel lblAno =
                new JLabel(
                        "ANO DE LANÇAMENTO:"
                );

        lblAno.setForeground(
                new Color(10, 86, 27)
        );

        lblAno.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        30
                )
        );

        lblAno.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        panel.add(
                lblAno,
                "cell 0 7 6 1,growx"
        );


        txtAno =
                new JTextField();

        txtAno.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        16
                )
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
                "cell 2 8,growx,h 42!"
        );


        // =====================================================
        // AUTOR
        // =====================================================

        JLabel lblAutor =
                new JLabel(
                        "AUTOR:"
                );

        lblAutor.setForeground(
                new Color(10, 86, 27)
        );

        lblAutor.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        lblAutor.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        30
                )
        );

        panel.add(
                lblAutor,
                "cell 2 10,growx"
        );


        // =====================================================
        // PAINEL DOS AUTORES
        // =====================================================

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


        // =====================================================
        // LISTA
        // =====================================================

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


        DefaultListModel<Autor> modeloAutores =
                new DefaultListModel<>();


        listaAutores =
                new JList<Autor>(
                        modeloAutores
                );


        listaAutores.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        18
                )
        );

        listaAutores.setBorder(
                null
        );


        /*
         * Mostra somente o nome do autor no JList.
         */
        listaAutores.setCellRenderer(
                new javax.swing.DefaultListCellRenderer() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public java.awt.Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Autor) {

                            Autor autor =
                                    (Autor) value;

                            setText(
                                    autor.getNome()
                            );
                        }

                        return this;
                    }
                }
        );


        painelListaAutores.add(
                listaAutores,
                "cell 0 0,grow"
        );


        // =====================================================
        // BOTÃO NOVO AUTOR
        // =====================================================

        botaoCadastrarAutor =
                new RoundedButton(
                        "Cadastrar novo autor"
                );


        painelListaAutores.add(
                botaoCadastrarAutor,
                "cell 0 1,growx,height 40!"
        );


        /*
         * AQUI ESTÁ A ALTERAÇÃO MAIS IMPORTANTE.
         *
         * Passamos "this" para Cadastro_Autor.
         *
         * Assim a tela de cadastro sabe qual
         * Cadastro_Livro deve atualizar.
         */
        botaoCadastrarAutor.addActionListener(
                new ActionListener() {

                    public void actionPerformed(
                            ActionEvent e) {

                        Cadastro_Autor cadastroAutor =
                                new Cadastro_Autor(
                                        Cadastro_Livro.this
                                );

                        cadastroAutor.setVisible(
                                true
                        );
                    }
                }
        );


        painelAutores.add(
                painelListaAutores,
                "cell 0 0,growx,height 140!"
        );


        // =====================================================
        // BOTÃO SELECIONAR
        // =====================================================

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
                            ActionEvent e) {

                        // Seleção dos autores
                    }
                }
        );


        panel.add(
                painelAutores,
                "cell 2 11,growx"
        );


        // =====================================================
        // GÊNERO
        // =====================================================

        JLabel lblGenero =
                new JLabel(
                        "GÊNERO:"
                );

        lblGenero.setForeground(
                new Color(10, 86, 27)
        );

        lblGenero.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        lblGenero.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        30
                )
        );

        panel.add(
                lblGenero,
                "cell 2 13,alignx center"
        );


        comboBox =
                new JComboBox();


        comboBox.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        11
                )
        );

        comboBox.setForeground(
                new Color(10, 89, 27)
        );

        comboBox.setBackground(
                Color.WHITE
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


        // =====================================================
        // ISBN
        // =====================================================

        JLabel lblISBN =
                new JLabel(
                        "ISBN:"
                );

        lblISBN.setForeground(
                new Color(10, 86, 27)
        );

        lblISBN.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        30
                )
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


        // =====================================================
        // BOTÃO CADASTRAR LIVRO
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
                        Cadastro_Livro.class.getResource(
                                "/imagens/BotaoCerto.png"
                        )
                )
        );


        panel.add(
                botaoCadastrar,
                "cell 2 19"
        );


        // =====================================================
        // PAINÉIS OCULTOS
        // =====================================================

        JPanel panel_1 =
                new JPanel();

        panel_1.setVisible(false);

        panel.add(
                panel_1,
                "cell 1 21,grow"
        );


        JPanel panel_2 =
                new JPanel();

        panel_2.setVisible(false);

        panel.add(
                panel_2,
                "cell 2 21"
        );


        JPanel panel_3 =
                new JPanel();

        panel_3.setVisible(false);

        panel.add(
                panel_3,
                "cell 3 21,grow"
        );


        // =====================================================
        // CARREGA AUTORES DO BANCO
        // =====================================================

        carregarAutoresDoBanco();
    }


    // =========================================================
    // CARREGAR AUTORES DO BANCO
    // =========================================================

    private void carregarAutoresDoBanco() {

        try {

            AutorDAO autorDAO =
                    new AutorDAO(
                            ConnectionFactory.getConnection()
                    );

            carregarAutores(
                    autorDAO.listarAutores()
            );

        } catch (Exception e) {

            e.printStackTrace();
        }
    }


    // =========================================================
    // CARREGAR AUTORES NO JLIST
    // =========================================================

    public void carregarAutores(
            List<Autor> autores) {

        DefaultListModel<Autor> modelo =
                (DefaultListModel<Autor>)
                        listaAutores.getModel();


        modelo.clear();


        if (autores == null) {
            return;
        }


        for (Autor autor :
                autores) {

            modelo.addElement(
                    autor
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


    public JTextField getTxtEditora() {

        return txtEditora;
    }


    public void setTxtEditora(
            JTextField txtEditora) {

        this.txtEditora =
                txtEditora;
    }


    public JComboBox getComboBox() {

        return comboBox;
    }


    public void setComboBox(
            JComboBox comboBox) {

        this.comboBox =
                comboBox;
    }


    public JButton getBtnAdicionar() {

        return botaoCadastrar;
    }


    public JList<Autor> getListaAutores() {

        return listaAutores;
    }


    public JTextField getTxtAno() {

        return txtAno;
    }


    public void setTxtAno(
            JTextField txtAno) {

        this.txtAno =
                txtAno;
    }


    public JButton getBotaoNovoAutor() {

        return botaoNovoAutor;
    }


    public JButton getBotaoCadastrarAutor() {

        return botaoCadastrarAutor;
    }
}
