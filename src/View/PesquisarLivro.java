package View;
 
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
 
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
 
import net.miginfocom.swing.MigLayout;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;

import dao.EmprestimoDAO;
import dao.UsuarioDAO;
import model.Autor;
import model.Livro;
import model.LivroTableModel;
 
public class PesquisarLivro extends JFrame {
 
    private static final long serialVersionUID = 1L;
 
    private JPanel contentPane;
    private JTextField textField;
    private JButton btnHome;
    private JButton btnPesquisar;
    private JScrollPane scrollLivros;
    private LivroTableModel livroTableModel;
    private String cpfUsuario; // usuário logado
 
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
 
    public PesquisarLivro() {
 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);
 
        // ===== PAINEL PRINCIPAL =====
        contentPane = new JPanel();
        contentPane.setBackground(new Color(175, 244, 198));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
 
        // ===== LAYOUT (5 colunas iguais, como em Meus Livros e Solicitações) =====
        contentPane.setLayout(
            new MigLayout("", "[200,grow][200,grow][200,grow][200,grow][200,grow]", "[][][][35.00,grow][105.00,grow][104.00,grow][][][grow]")
        );
 
        // ===== BOTÃO HOME =====
        btnHome = new JButton("");
        btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));
        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(new Color(10, 86, 27));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        btnHome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");
 
        // ===== TÍTULO (centralizado) =====
        JLabel lblNewLabel = new JLabel("Pesquisar Livros");
        lblNewLabel.setForeground(new Color(10, 86, 27));
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 51));
        contentPane.add(lblNewLabel, "cell 0 1 5 1,alignx center");
 
        // ===== CAMPO DE PESQUISA (centralizado) =====
        textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textField.setColumns(10);
        // Enter no campo faz o mesmo que clicar em Pesquisar
        textField.addActionListener(e -> btnPesquisar.doClick());
        contentPane.add(textField, "flowx,cell 0 3 5 1,alignx center,width 500!,height 42!");
 
        // ===== BOTÃO PESQUISAR (menor, ao lado da caixa) =====
        ImageIcon pesquisar = new ImageIcon(PesquisarLivro.class.getResource("/imagens/pesquisar.png"));
        Image imagemPesquisar = pesquisar.getImage().getScaledInstance(180, 65, Image.SCALE_SMOOTH);
 
        btnPesquisar = new JButton("");
        btnPesquisar.setBorderPainted(false);
        btnPesquisar.setContentAreaFilled(false);
        btnPesquisar.setFocusPainted(false);
        btnPesquisar.setIcon(new ImageIcon(imagemPesquisar));
        btnPesquisar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ação do botão pesquisar
            }
        });
        contentPane.add(btnPesquisar, "cell 0 3,width 190!,height 75!");
 
        // Redimensiona a imagem junto com o botão
        btnPesquisar.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int largura = btnPesquisar.getWidth();
                int altura = btnPesquisar.getHeight();
                if (largura > 0 && altura > 0) {
                    Image novaImagem = pesquisar.getImage().getScaledInstance(
                        Math.max(1, largura - 10),
                        Math.max(1, altura - 10),
                        Image.SCALE_SMOOTH
                    );
                    btnPesquisar.setIcon(new ImageIcon(novaImagem));
                }
            }
        });
 
        // ===== LOGO (mesma posição de Meus Livros e Solicitações) =====
        JLabel lblLogo = new JLabel("");
        lblLogo.setIcon(new ImageIcon(PesquisarLivro.class.getResource("/imagens/Logo.png")));
        contentPane.add(lblLogo, "cell 1 0 3 1,alignx center,aligny top");

        // ===== TABELA COM OS LIVROS ENCONTRADOS =====
        livroTableModel = new LivroTableModel();
        JTable tabelaLivros = new JTable(livroTableModel);
        tabelaLivros.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tabelaLivros.setRowHeight(32);
        tabelaLivros.setDefaultEditor(Object.class, null); // não deixa editar
        tabelaLivros.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaLivros.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tabelaLivros.setToolTipText("Clique em um livro para ver as informações");

        // Cores verdes iguais ao resto do projeto
        tabelaLivros.setBackground(new Color(175, 244, 198));
        tabelaLivros.setForeground(new Color(30, 30, 30));
        tabelaLivros.setGridColor(new Color(24, 125, 45));
        tabelaLivros.setSelectionBackground(new Color(114, 219, 145));
        tabelaLivros.setSelectionForeground(Color.BLACK);
        tabelaLivros.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
                setBorder(new EmptyBorder(0, 10, 0, 10));
                if (!isSelected) {
                    // Linhas alternadas em dois tons de verde
                    setBackground(row % 2 == 0 ? new Color(175, 244, 198) : new Color(204, 250, 219));
                }
                return this;
            }
        });

        // Cabeçalho verde escuro com letra branca
        JTableHeader cabecalho = tabelaLivros.getTableHeader();
        cabecalho.setReorderingAllowed(false);
        cabecalho.setDefaultRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, false, false, row, column);
                setBackground(new Color(10, 86, 27));
                setForeground(Color.WHITE);
                setFont(new Font("Tahoma", Font.BOLD, 16));
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(24, 125, 45)));
                return this;
            }
        });
        cabecalho.setPreferredSize(new Dimension(0, 36));

        // Clicar no livro mostra as informações dele e de quem cadastrou
        tabelaLivros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaLivros.rowAtPoint(e.getPoint());
                if (linha >= 0) {
                    mostrarInfo(livroTableModel.getLivro(tabelaLivros.convertRowIndexToModel(linha)));
                }
            }
        });

        scrollLivros = new JScrollPane(tabelaLivros);
        scrollLivros.getViewport().setBackground(new Color(175, 244, 198));
        scrollLivros.setBorder(BorderFactory.createLineBorder(new Color(10, 86, 27), 2));
        scrollLivros.setVisible(false); // só aparece depois de pesquisar
        contentPane.add(scrollLivros, "cell 0 4 5 3,grow");
 
        // ===== LOCALIZAÇÃO =====
        JButton btnLocalizacao = new JButton("Gaspar");
        btnLocalizacao.setFont(new Font("Tahoma", Font.BOLD, 36));
        btnLocalizacao.setBorderPainted(false);
        btnLocalizacao.setContentAreaFilled(false);
        btnLocalizacao.setIcon(new ImageIcon(PesquisarLivro.class.getResource("/imagens/LogoLocalizacao.png")));
        btnLocalizacao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Ação da localização
            }
        });
        contentPane.add(btnLocalizacao, "cell 0 8 5 1,alignx left,aligny bottom");
    }
 
    // ===== GETTERS =====
    public JButton getBtnHome() {
        return btnHome;
    }
 
    public JButton getBtnPesquisar() {
        return btnPesquisar;
    }
 
    public String getTextoPesquisa() {
        return textField.getText().trim();
    }
 
    // Mostra os livros na tabela (esconde a logo para dar espaço)
    public void mostrarLivros(List<Livro> livros) {
        livroTableModel.setLista(livros);
        scrollLivros.setVisible(true);
        contentPane.revalidate();
        contentPane.repaint();
    }

    public void limparPesquisa() {
        textField.setText("");
    }

    // =====================================================
    // MOSTRA AS INFORMAÇÕES DO LIVRO E DE QUEM CADASTROU
    // =====================================================

    private void mostrarInfo(Livro livro) {

        String autores = "";

        if (livro.getAutores() != null) {
            for (Autor a : livro.getAutores()) {
                if (!autores.isEmpty()) {
                    autores += ", ";
                }
                autores += a.getNome();
            }
        }

        // Busca os dados do dono do livro
        String nomeDono = "Não encontrado";
        String emailDono = "-";
        String telefoneDono = "-";

        try {
            String[] dono = new UsuarioDAO().buscarContatoPorCpf(livro.getCpfDono());
            if (dono != null) {
                nomeDono = dono[0];
                emailDono = dono[1];
                telefoneDono = dono[2];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Livro cadastrado pelo próprio usuário não pode ser pedido emprestado
        boolean livroProprio = cpfUsuario != null && cpfUsuario.equals(livro.getCpfDono());

        String texto =
                "<html><div style='width:360px'>"
                + "<span style='font-size:24px; color:#0A561B'><b>" + livro.getNome() + "</b></span><br><br>"
                + "<b>Autor(es):</b> " + autores + "<br>"
                + "<b>Editora:</b> " + livro.getEditora() + "<br>"
                + "<b>Ano de lançamento:</b> " + livro.getAnoLancamento() + "<br>"
                + "<b>Gênero:</b> " + livro.getGenero() + "<br>"
                + "<b>ISBN:</b> " + livro.getIsbn() + "<br><br>"
                + "<span style='font-size:19px; color:#0A561B'><b>Cadastrado por</b></span><br>"
                + "<b>Nome:</b> " + nomeDono + "<br>"
                + "<b>E-mail:</b> " + emailDono + "<br>"
                + "<b>Telefone:</b> " + telefoneDono
                + (livroProprio ? "<br><br><i>Este livro é seu.</i>" : "")
                + "</div></html>";

        JLabel lblTexto = new JLabel(texto);
        lblTexto.setFont(new Font("Tahoma", Font.PLAIN, 17));
        lblTexto.setForeground(new Color(30, 30, 30));
        lblTexto.setBorder(new EmptyBorder(0, 15, 0, 0));

        String[] opcoes = livroProprio
                ? new String[] { "Fechar" }
                : new String[] { "Solicitar empréstimo", "Fechar" };

        int escolha = mostrarJanela(
                lblTexto,
                livro.getNome(),
                JOptionPane.PLAIN_MESSAGE,
                new ImageIcon(TelaMeusLivros.pegarImagem(livro, 260, 360)),
                opcoes);

        // 0 = clicou em "Solicitar empréstimo"
        if (!livroProprio && escolha == 0) {
            solicitarEmprestimo(livro);
        }
    }

    // =====================================================
    // SOLICITA O EMPRÉSTIMO DO LIVRO
    // =====================================================

    private void solicitarEmprestimo(Livro livro) {

        if (cpfUsuario == null) {
            mensagem("Faça login para solicitar um empréstimo.");
            return;
        }

        try {

            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();

            if (!emprestimoDAO.livroDisponivel(livro.getIsbn())) {
                mensagem("\"" + livro.getNome() + "\" já está emprestado no momento.");
                return;
            }

            if (emprestimoDAO.jaSolicitou(cpfUsuario, livro.getIsbn())) {
                mensagem("Você já solicitou o empréstimo de \"" + livro.getNome() + "\".");
                return;
            }

            emprestimoDAO.solicitar(cpfUsuario, livro.getIsbn());

            mensagem("Solicitação de empréstimo de \"" + livro.getNome() + "\" enviada!\n"
                    + "Aguarde o dono do livro aceitar.");

        } catch (Exception e) {

            e.printStackTrace();
            mensagem("Erro ao solicitar empréstimo:\n" + e.getMessage());
        }
    }

    public void setCpfUsuario(String cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    // =====================================================
    // JANELAS VERDES COM BOTÕES ARREDONDADOS (IGUAL A "MEUS LIVROS")
    // Devolve o número do botão clicado (0, 1, ...) ou -1 se fechou no X
    // =====================================================

    private void mensagem(String texto) {
        mostrarJanela(texto, "Mensagem", JOptionPane.INFORMATION_MESSAGE, null, "OK");
    }

    private int mostrarJanela(Object conteudo, String titulo, int tipo, Icon icone, String... opcoes) {

        UIManager.put("OptionPane.background", new Color(175, 244, 198));
        UIManager.put("Panel.background", new Color(175, 244, 198));

        JOptionPane painel = new JOptionPane(conteudo, tipo, JOptionPane.DEFAULT_OPTION, icone);

        JButton[] botoes = new JButton[opcoes.length];

        for (int i = 0; i < opcoes.length; i++) {
            JButton botao = new TelaMeusLivros.BotaoArredondado(opcoes[i], 40);
            botao.setBackground(new Color(114, 219, 145));
            botao.setForeground(Color.BLACK);
            botao.setFont(new Font("Tahoma", Font.BOLD, 15));
            botao.setMargin(new Insets(6, 18, 6, 18));
            botao.addActionListener(e -> painel.setValue(botao));
            botoes[i] = botao;
        }

        painel.setOptions(botoes);
        painel.setInitialValue(botoes[botoes.length - 1]);

        JDialog janela = painel.createDialog(this, titulo);
        janela.setVisible(true);
        janela.dispose();

        UIManager.put("OptionPane.background", null);
        UIManager.put("Panel.background", null);

        for (int i = 0; i < botoes.length; i++) {
            if (painel.getValue() == botoes[i]) {
                return i;
            }
        }

        return -1;
    }
}
