package View;



import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import model.Autor;
import net.miginfocom.swing.MigLayout;

public class Lista_Autores extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;

    private JList<Autor> listaAutores;

    private DefaultListModel<Autor> modeloAutores;

    private JButton botaoSelecionar;

    private JButton botaoNovoAutor;

    private JButton botaoEditar;

    private JButton botaoExcluir;

    private JButton btnVoltar;


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
    // CONSTRUTOR
    // =========================================================

    public Lista_Autores() {

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
                new MigLayout("", "[grow][700!][grow]", "[grow][80!][grow][60!][][grow]")
        );


        // =====================================================
        // BOTÃO VOLTAR
        // =====================================================

        btnVoltar = new JButton("");

        btnVoltar.setIcon(
                new ImageIcon(Lista_Autores.class.getResource("/imagens/Voltar.png"))
        );

        btnVoltar.setFont(new Font("Tahoma", Font.PLAIN, 28));

        btnVoltar.setForeground(new Color(10, 86, 27));

        btnVoltar.setBorderPainted(false);

        btnVoltar.setContentAreaFilled(false);

        btnVoltar.setFocusPainted(false);

        // Fecha a lista e volta para o cadastro de livro
        btnVoltar.addActionListener(
                e -> dispose()
        );

        contentPane.add(btnVoltar, "cell 0 0,alignx left,aligny top");


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

        contentPane.add(
                botaoNovoAutor,
                "flowx,cell 1 3,growx"
        );


        // =====================================================
        // BOTÕES ALTERAR E EXCLUIR
        // =====================================================

        botaoEditar =
                new RoundedButton(
                        "Alterar autor"
                );

        contentPane.add(
                botaoEditar,
                "cell 1 3,growx"
        );

        botaoExcluir =
                new RoundedButton(
                        "Excluir autor"
                );

        contentPane.add(
                botaoExcluir,
                "cell 1 3,growx"
        );


        // =====================================================
        // BOTÃO SELECIONAR
        // =====================================================

        botaoSelecionar =
                new RoundedButton(
                        "Selecionar autor(es)"
                );

        contentPane.add(
                botaoSelecionar,
                "cell 1 4,growx"
        );
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
    // GETTERS
    // =========================================================

    public List<Autor> getAutoresSelecionados() {

        return listaAutores.getSelectedValuesList();
    }


    public JList<Autor> getListaAutores() {

        return listaAutores;
    }


    public JButton getBotaoSelecionar() {

        return botaoSelecionar;
    }


    public JButton getBotaoNovoAutor() {

        return botaoNovoAutor;
    }


    public JButton getBotaoEditar() {

        return botaoEditar;
    }


    public JButton getBotaoExcluir() {

        return botaoExcluir;
    }


    public JButton getBtnVoltar() {

        return btnVoltar;
    }
}
