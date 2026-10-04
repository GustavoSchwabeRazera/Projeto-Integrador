package View;



import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import dao.AutorDAO;
import dao.ConnectionFactory;
import model.Autor;
import net.miginfocom.swing.MigLayout;

public class Lista_Autores extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JList<Autor> listaAutores;

    private DefaultListModel<Autor> modeloAutores;

    private JButton botaoSelecionar;

    private JButton botaoNovoAutor;

    private Cadastro_Livro cadastroLivro;


    // =========================================================
    // MAIN
    // =========================================================

    public static void main(String[] args) {

        EventQueue.invokeLater(() -> {

            try {

                Lista_Autores frame =
                        new Lista_Autores();

                frame.setVisible(true);

            } catch (Exception e) {

                e.printStackTrace();
            }
        });
    }


    // =========================================================
    // CONSTRUTOR PADRÃO
    // =========================================================

    public Lista_Autores() {

        this(null);
    }


    // =========================================================
    // CONSTRUTOR
    // =========================================================

    public Lista_Autores(
            Cadastro_Livro cadastroLivro) {

        this.cadastroLivro =
                cadastroLivro;


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
                        "[grow][700!][grow]",
                        "[grow][80!][grow][60!][grow]"
                )
        );


        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel titulo =
                new JLabel(
                        "LISTA DE AUTORES"
                );

        titulo.setForeground(
                new Color(10, 86, 27)
        );

        titulo.setFont(
                new Font(
                        "Tahoma",
                        Font.BOLD,
                        40
                )
        );

        titulo.setHorizontalAlignment(
                SwingConstants.CENTER
        );

        contentPane.add(
                titulo,
                "cell 1 1,growx"
        );


        // =====================================================
        // MODELO DA LISTA
        // =====================================================

        modeloAutores =
                new DefaultListModel<>();


        listaAutores =
                new JList<>(
                        modeloAutores
                );


        listaAutores.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        20
                )
        );

        listaAutores.setForeground(
                new Color(10, 86, 27)
        );

        listaAutores.setBackground(
                Color.WHITE
        );


        // =====================================================
        // PERMITE SELECIONAR VÁRIOS AUTORES
        // =====================================================

        listaAutores.setSelectionMode(
                ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
        );


        JScrollPane scrollPane =
                new JScrollPane(
                        listaAutores
                );

        contentPane.add(
                scrollPane,
                "cell 1 2,grow"
        );
        
        


        // =====================================================
        // BOTÃO CADASTRAR NOVO AUTOR
        // =====================================================

        botaoNovoAutor =
                new RoundedButton(
                        "Cadastrar novo autor"
                );

        botaoNovoAutor.addActionListener(
                e -> abrirCadastroAutor()
        );

        contentPane.add(
                botaoNovoAutor,
                "cell 1 3,growx"
        );


        // =====================================================
        // BOTÃO SELECIONAR
        // =====================================================

        botaoSelecionar =
                new RoundedButton(
                        "Selecionar autor(es)"
                );

        botaoSelecionar.addActionListener(
                e -> selecionarAutores()
        );

        contentPane.add(
                botaoSelecionar,
                "cell 1 4,growx"
        );


        // =====================================================
        // CARREGAR AUTORES
        // =====================================================

        carregarAutoresDoBanco();
    }


    // =========================================================
    // ABRIR CADASTRO DE AUTOR
    // =========================================================

	    private void abrirCadastroAutor() {
	
	        Cadastro_Autor cadastroAutor =
	                new Cadastro_Autor(this);
	
	        cadastroAutor.setVisible(true);
	    }


    // =========================================================
    // CARREGAR AUTORES
    // =========================================================

    private void carregarAutoresDoBanco() {

        try {

            AutorDAO autorDAO =
                    new AutorDAO(
                            ConnectionFactory.getConnection()
                    );

            List<Autor> autores =
                    autorDAO.listarAutores();

            carregarAutores(
                    autores
            );


        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Erro ao carregar os autores:\n"
                            + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // =========================================================
    // RECARREGAR AUTORES
    // =========================================================

    public void recarregarAutores() {

        carregarAutoresDoBanco();
    }


    // =========================================================
    // CARREGAR NA JLIST
    // =========================================================

    public void carregarAutores(
            List<Autor> autores) {

        modeloAutores.clear();

        if (autores == null) {

            return;
        }

        for (Autor autor : autores) {

            modeloAutores.addElement(
                    autor
            );
        }
    }


    // =========================================================
    // SELECIONAR AUTORES
    // =========================================================

    private void selecionarAutores() {

        List<Autor> selecionados =
                listaAutores.getSelectedValuesList();


        if (selecionados == null
                || selecionados.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Selecione pelo menos um autor."
            );

            return;
        }


        // =====================================================
        // DEVOLVE PARA O CADASTRO DE LIVRO
        // =====================================================

        if (cadastroLivro != null) {

            cadastroLivro.receberAutoresSelecionados(
                    new ArrayList<>(
                            selecionados
                    )
            );
        }


        // Fecha a janela
        dispose();
    }


    // =========================================================
    // GETTERS
    // =========================================================

    public JList<Autor> getListaAutores() {

        return listaAutores;
    }


    public JButton getBotaoSelecionar() {

        return botaoSelecionar;
    }


    public JButton getBotaoNovoAutor() {

        return botaoNovoAutor;
    }


    public Cadastro_Livro getCadastroLivro() {

        return cadastroLivro;
    }


    public void setCadastroLivro(
            Cadastro_Livro cadastroLivro) {

        this.cadastroLivro =
                cadastroLivro;
    }
}