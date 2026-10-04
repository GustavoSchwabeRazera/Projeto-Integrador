package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import dao.ConnectionFactory;
import dao.LivroDAO;
import model.Autor;
import model.Livro;
import net.miginfocom.swing.MigLayout;

public class Cadastro_Livro extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JTextField txtNome;
    private JTextField txtEditora;
    private JTextField txtAno;
    private JTextField textField;

    private JComboBox<Generos> comboBox;

    private JButton botaoCadastrar;
    private JButton botaoNovoAutor;
    private JButton botaoFoto;

    // Foto da capa escolhida (em bytes)
    private byte[] fotoSelecionada;

    // CPF de quem está logado (o controller preenche)
    private String cpfUsuario;

    // =====================================================
    // BOTÃO HOME / VOLTAR
    // =====================================================

    private JButton btnVoltar;

    // Guarda os autores selecionados
    private List<Autor> autoresSelecionados = new ArrayList<>();


    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            try {

                Cadastro_Livro frame = new Cadastro_Livro();
                frame.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }


    // =====================================================
    // CONSTRUTOR
    // =====================================================

    public Cadastro_Livro() {

        setBackground(new Color(128, 255, 0));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setSize(1920, 1080);

        setLocationRelativeTo(null);


        // =================================================
        // CONTENT PANE
        // =================================================

        contentPane = new JPanel();

        contentPane.setBackground(new Color(175, 244, 198));

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);

        contentPane.setLayout(
                new MigLayout(
                        "",
                        "[98.00][86.00][150.00,grow][51.00][118.00]",
                        "[][][][][][grow][][][][]"
                )
        );


        // =================================================
        // BOTÃO VOLTAR
        // =================================================

        btnVoltar = new JButton("");

        btnVoltar.setIcon(
                new ImageIcon(Cadastro_Livro.class.getResource("/imagens/Voltar.png"))
        );

        btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 28));

        btnVoltar.setForeground(new Color(10, 86, 27));

        btnVoltar.setBorderPainted(false);

        btnVoltar.setContentAreaFilled(false);

        btnVoltar.setFocusPainted(false);

        contentPane.add(btnVoltar, "cell 0 0,alignx left,aligny top");


        // =================================================
        // LOGO
        // =================================================

        ImageIcon logoOriginal =
                new ImageIcon(
                        Cadastro_Livro.class.getResource("/imagens/Logo.png")
                );

        Image logoRedimensionada =
                logoOriginal.getImage()
                        .getScaledInstance(
                                300,
                                150,
                                Image.SCALE_SMOOTH
                        );


        // =================================================
        // TÍTULO DA TELA
        // =================================================

        JLabel lblTituloTela = new JLabel("Cadastro de Livros:");

        lblTituloTela.setForeground(new Color(10, 86, 27));

        lblTituloTela.setFont(new Font("Tahoma", Font.BOLD, 48));

        contentPane.add(lblTituloTela, "cell 2 1,alignx center,aligny bottom");


        // =================================================
        // PAINEL PRINCIPAL
        // =================================================

        JPanel panel = new ImagePanel();

        panel.setForeground(Color.WHITE);

        panel.setBackground(new Color(10, 86, 27));

        panel.setOpaque(false);

        contentPane.add(panel, "cell 2 5,grow");

        panel.setLayout(
                new MigLayout(
                        "",
                        "[234.00][10.00,grow][733.00,grow,center][grow][83.00][165.00]",
                        "[73.00][][][28.00][][][][][][][][grow][27.00][][][][31.00][35.00][][][][grow]"
                )
        );


        // =================================================
        // TÍTULO DO LIVRO
        // =================================================

        JLabel lblTitulo = new JLabel("TÍTULO DO LIVRO:");

        lblTitulo.setForeground(new Color(10, 86, 27));

        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 30));

        panel.add(lblTitulo, "cell 0 1 6 1,alignx center");


        txtNome = new JTextField();

        txtNome.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        txtNome.setColumns(10);

        txtNome.setBorder(
                BorderFactory.createLineBorder(new Color(10, 86, 27), 2)
        );

        panel.add(txtNome, "cell 2 2,growx,h 42!");


        // =================================================
        // EDITORA
        // =================================================

        JLabel lblEditora = new JLabel("EDITORA:");

        lblEditora.setForeground(new Color(10, 86, 27));

        lblEditora.setHorizontalAlignment(SwingConstants.CENTER);

        lblEditora.setFont(new Font("Tahoma", Font.BOLD, 30));

        panel.add(lblEditora, "cell 0 4 6 1,growx");


        txtEditora = new JTextField();

        txtEditora.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        txtEditora.setColumns(10);

        txtEditora.setBorder(
                BorderFactory.createLineBorder(new Color(10, 86, 27), 2)
        );

        panel.add(txtEditora, "cell 2 5,growx,height 42!");


        // =================================================
        // ANO
        // =================================================

        JLabel lblAno = new JLabel("ANO DE LANÇAMENTO:");

        lblAno.setForeground(new Color(10, 86, 27));

        lblAno.setFont(new Font("Tahoma", Font.BOLD, 30));

        lblAno.setHorizontalAlignment(SwingConstants.CENTER);

        panel.add(lblAno, "cell 0 7 6 1,growx");


        txtAno = new JTextField();

        txtAno.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        txtAno.setColumns(10);

        txtAno.setBorder(
                BorderFactory.createLineBorder(new Color(10, 86, 27), 2)
        );

        panel.add(txtAno, "cell 2 8,growx,h 42!");


        // =================================================
        // AUTOR
        // =================================================

        JLabel lblAutor = new JLabel("AUTOR:");

        lblAutor.setForeground(new Color(10, 86, 27));

        lblAutor.setHorizontalAlignment(SwingConstants.CENTER);

        lblAutor.setFont(new Font("Tahoma", Font.BOLD, 30));

        panel.add(lblAutor, "cell 2 10,growx");


        // =================================================
        // BOTÃO SELECIONAR AUTORES
        // =================================================

        botaoNovoAutor = new RoundedButton("Selecionar autor(es)");

        panel.add(botaoNovoAutor, "cell 2 11,growx,height 45!");

        botaoNovoAutor.addActionListener(e -> abrirListaAutores());


        // =================================================
        // GÊNERO
        // =================================================

        JLabel lblGenero = new JLabel("GÊNERO:");

        lblGenero.setForeground(new Color(10, 86, 27));

        lblGenero.setHorizontalAlignment(SwingConstants.CENTER);

        lblGenero.setFont(new Font("Tahoma", Font.BOLD, 30));

        panel.add(lblGenero, "cell 2 14,alignx center");


        comboBox = new JComboBox<>();

        comboBox.setFont(new Font("Tahoma", Font.BOLD, 14));

        comboBox.setForeground(new Color(10, 89, 27));

        comboBox.setBackground(Color.WHITE);

        comboBox.setBorder(
                BorderFactory.createLineBorder(new Color(10, 86, 27), 2)
        );

        comboBox.setModel(new DefaultComboBoxModel<>(Generos.values()));

        panel.add(comboBox, "cell 2 15,growx,height 42!");


        // =================================================
        // ISBN
        // =================================================

        JLabel lblISBN = new JLabel("ISBN:");

        lblISBN.setForeground(new Color(10, 86, 27));

        lblISBN.setFont(new Font("Tahoma", Font.BOLD, 30));

        panel.add(lblISBN, "cell 2 17");


        textField = new JTextField();

        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        textField.setColumns(10);

        textField.setBorder(
                BorderFactory.createLineBorder(new Color(10, 86, 27), 2)
        );

        panel.add(textField, "cell 2 18,growx,height 42!");


        // =================================================
        // FOTO DA CAPA
        // =================================================

        botaoFoto = new RoundedButton("Adicionar foto da capa");

        panel.add(botaoFoto, "cell 2 19,growx,height 60!");

        botaoFoto.addActionListener(e -> escolherFoto());


        // =================================================
        // BOTÃO CADASTRAR
        // =================================================

        botaoCadastrar = new JButton("");

        botaoCadastrar.setContentAreaFilled(false);

        botaoCadastrar.setBorderPainted(false);

        botaoCadastrar.setIcon(
                new ImageIcon(Cadastro_Livro.class.getResource("/imagens/BotaoCerto.png"))
        );

        panel.add(botaoCadastrar, "cell 2 20");

        // O clique do botão cadastrar é tratado no LivroController
    }


    // =====================================================
    // ESCOLHER FOTO DA CAPA
    // =====================================================

    private void escolherFoto() {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setFileFilter(
                new FileNameExtensionFilter(
                        "Imagens (*.png, *.jpg, *.jpeg, *.gif)",
                        "png", "jpg", "jpeg", "gif"
                )
        );

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            File arquivo = fileChooser.getSelectedFile();

            try {

                BufferedImage original = ImageIO.read(arquivo);

                if (original == null) {
                    mostrarMensagem("Esse arquivo não é uma imagem válida.");
                    return;
                }

                // Diminui a foto para 270x340 (fica pequena e cabe no banco)
                BufferedImage pequena =
                        new BufferedImage(270, 340, BufferedImage.TYPE_INT_RGB);

                Graphics2D g = pequena.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g.drawImage(original, 0, 0, 270, 340, null);
                g.dispose();

                // Converte para bytes (JPG) para salvar no banco
                ByteArrayOutputStream saida = new ByteArrayOutputStream();
                ImageIO.write(pequena, "jpg", saida);
                fotoSelecionada = saida.toByteArray();

                // Mostra uma miniatura no botão
                Image mini = new ImageIcon(fotoSelecionada)
                        .getImage()
                        .getScaledInstance(40, 50, Image.SCALE_SMOOTH);

                botaoFoto.setIcon(new ImageIcon(mini));
                botaoFoto.setText("Foto selecionada (clique para trocar)");

            } catch (Exception ex) {

                ex.printStackTrace();

                mostrarMensagem("Erro ao carregar a imagem.");
            }
        }
    }


    // =====================================================
    // ABRIR LISTA DE AUTORES
    // =====================================================

    private void abrirListaAutores() {

        Lista_Autores lista = new Lista_Autores(this);

        lista.setVisible(true);
    }


    // =====================================================
    // RECEBER AUTORES
    // =====================================================

    public void receberAutoresSelecionados(List<Autor> autores) {

        autoresSelecionados.clear();

        if (autores != null) {

            autoresSelecionados.addAll(autores);
        }

        if (autoresSelecionados.isEmpty()) {

            botaoNovoAutor.setText("Selecionar autor(es)");

        } else {

            botaoNovoAutor.setText(
                    autoresSelecionados.size() + " autor(es) selecionado(s)"
            );
        }
    }


    // =====================================================
    // CADASTRAR LIVRO
    // =====================================================

    // Retorna true se o livro foi salvo
    public boolean cadastrarLivro() {

        String titulo = txtNome.getText().trim();

        String editora = txtEditora.getText().trim();

        String anoTexto = txtAno.getText().trim();

        String isbn = textField.getText().trim();

        String genero = "";

        if (comboBox.getSelectedItem() != null) {

            genero = comboBox.getSelectedItem().toString().trim();
        }


        // =================================================
        // VALIDAÇÕES
        // =================================================

        if (titulo.isEmpty()) {

            mostrarMensagem("Digite o título do livro.");

            txtNome.requestFocus();

            return false;
        }


        if (editora.isEmpty()) {

            mostrarMensagem("Digite a editora.");

            txtEditora.requestFocus();

            return false;
        }


        if (anoTexto.isEmpty()) {

            mostrarMensagem("Digite o ano de lançamento.");

            txtAno.requestFocus();

            return false;
        }


        int ano;

        try {

            ano = Integer.parseInt(anoTexto);

        } catch (NumberFormatException e) {

            mostrarMensagem("Digite um ano válido.");

            txtAno.requestFocus();

            return false;
        }


        if (ano < 1 || ano > 9999) {

            mostrarMensagem("Digite um ano válido.");

            txtAno.requestFocus();

            return false;
        }


        if (isbn.isEmpty()) {

            mostrarMensagem("Digite o ISBN.");

            textField.requestFocus();

            return false;
        }


        if (!isbn.matches("\\d{13}")) {

            mostrarMensagem("O ISBN deve conter exatamente 13 números.");

            textField.requestFocus();

            return false;
        }


        if (genero.isEmpty()) {

            mostrarMensagem("Selecione o gênero.");

            return false;
        }


        if (autoresSelecionados.isEmpty()) {

            mostrarMensagem("Selecione pelo menos um autor.");

            return false;
        }


        // =================================================
        // CRIAR LIVRO
        // =================================================

        Livro livro =
                new Livro(
                        isbn,
                        titulo,
                        editora,
                        ano,
                        genero,
                        autoresSelecionados
                );

        livro.setFotoCapa(fotoSelecionada);

        livro.setCpfDono(cpfUsuario);


        // =================================================
        // SALVAR
        // =================================================

        try {

            LivroDAO livroDAO =
                    new LivroDAO(ConnectionFactory.getConnection());

            livroDAO.inserir(livro);


            mostrarMensagem("Livro cadastrado com sucesso!");


            // Limpar campos

            txtNome.setText("");

            txtEditora.setText("");

            txtAno.setText("");

            textField.setText("");

            autoresSelecionados.clear();

            botaoNovoAutor.setText("Selecionar autor(es)");

            fotoSelecionada = null;

            botaoFoto.setIcon(null);

            botaoFoto.setText("Adicionar foto da capa");

            return true;


        } catch (Exception ex) {

            ex.printStackTrace();

            mostrarMensagem(
                    "Erro ao cadastrar o livro:\n" + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );

            return false;
        }
    }


    // =====================================================
    // MENSAGENS CUSTOMIZADAS (FUNDO VERDE)
    // =====================================================

    private void mostrarMensagem(String mensagem) {
        UIManager.put("OptionPane.background", new Color(175, 244, 198));
        UIManager.put("Panel.background", new Color(175, 244, 198));

        JOptionPane.showMessageDialog(this, mensagem);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
    }

    private void mostrarMensagem(String mensagem, String titulo, int tipoMensagem) {
        UIManager.put("OptionPane.background", new Color(175, 244, 198));
        UIManager.put("Panel.background", new Color(175, 244, 198));

        JOptionPane.showMessageDialog(this, mensagem, titulo, tipoMensagem);

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);
    }


    // =====================================================
    // GETTERS E SETTERS
    // =====================================================

    public void setCpfUsuario(String cpfUsuario) {

        this.cpfUsuario = cpfUsuario;
    }


    public JButton getBtnVoltar() {

        return btnVoltar;
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


    public JTextField getTextField() {

        return textField;
    }


    public JComboBox<Generos> getComboBox() {

        return comboBox;
    }


    public JButton getBtnAdicionar() {

        return botaoCadastrar;
    }


    public JButton getBotaoNovoAutor() {

        return botaoNovoAutor;
    }


    public List<Autor> getAutoresSelecionados() {

        return autoresSelecionados;
    }
}