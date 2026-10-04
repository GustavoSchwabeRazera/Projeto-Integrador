package View;

import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Dimension;
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

public class PesquisarLivro extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTextField textField;
    private JButton btnHome;
    private JButton btnPesquisar;

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

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        setBounds(100, 100, 1920, 1080);


        // =====================================================
        // PAINEL PRINCIPAL
        // =====================================================

        contentPane = new JPanel();

        contentPane.setBackground(
            new Color(175, 244, 198)
        );

        contentPane.setBorder(
            new EmptyBorder(5, 5, 5, 5)
        );

        setContentPane(contentPane);


        // =====================================================
        // LAYOUT
        // =====================================================

        contentPane.setLayout(
            new MigLayout("", "[][][][-175.00,grow][80.00][45.00][906.00][210.00]", "[][][35.00,grow][105.00,grow][104.00,grow][][][grow]")
        );


        // =====================================================
        // LOGO
        // =====================================================

        JLabel lblNewLabel_3 = new JLabel("");

        ImageIcon logo = new ImageIcon(
            PesquisarLivro.class.getResource(
                "/imagens/Logo.png"
            )
        );

        Image imagemRedimensionada = logo.getImage()
            .getScaledInstance(
                350,
                190,
                Image.SCALE_SMOOTH
            );


        // =====================================================
        // IMAGEM DO PERFIL
        // =====================================================

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


        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel lblNewLabel = new JLabel(
            "Pesquisar Livros"
        );
        lblNewLabel.setForeground(new Color(10, 86, 27));

        lblNewLabel.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        lblNewLabel.setFont(
            new Font(
                "Tahoma",
                Font.BOLD,
                51
            )
        );

        contentPane.add(
            lblNewLabel,
            "cell 0 1 8 1,alignx center"
        );


        // =====================================================
        // LOGO CENTRAL
        // =====================================================

        lblNewLabel_3.setIcon(
            new ImageIcon(
                imagemRedimensionada
            )
        );

        contentPane.add(
            lblNewLabel_3,
            "flowy,cell 0 3 8 1,alignx center"
        );


        // =====================================================
        // BOTÃO HOME
        // =====================================================

        btnHome = new JButton("");
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});

		btnHome.setIcon(new ImageIcon(TelaMeusLivros.class.getResource("/imagens/casa 1.png")));

		btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));

		btnHome.setForeground(new Color(10, 86, 27));

		btnHome.setBorderPainted(false);
		btnHome.setContentAreaFilled(false);
		btnHome.setFocusPainted(false);

		contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

		ImageIcon perfil1 = new ImageIcon(TelaMeusLivros.class.getResource("/imagens/perfil3.png"));

		Image imgPerfil = perfil1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);


        // =====================================================
        // CAMPO DE PESQUISA
        // =====================================================

        textField = new JTextField();

        textField.setFont(
            new Font(
                "Segoe UI",
                Font.PLAIN,
                16
            )
        );

        textField.setColumns(10);


        // =====================================================
        // TAMANHO DA CAIXA DE TEXTO
        // =====================================================
        //
        // 600 = tamanho preferencial
        // 200 = tamanho mínimo
        // 800 = tamanho máximo
        //
        // =====================================================

        contentPane.add(
            textField,
            "cell 0 4 8 1,width 600:200:800,alignx center,height 42!"
        );


        // =====================================================
        // BOTÃO PESQUISAR
        // =====================================================

        btnPesquisar = new JButton("");

        btnPesquisar.setBorderPainted(false);

        btnPesquisar.setContentAreaFilled(false);

        btnPesquisar.setFocusPainted(false);


        // =====================================================
        // IMAGEM ORIGINAL DO BOTÃO
        // =====================================================

        ImageIcon pesquisar = new ImageIcon(
            PesquisarLivro.class.getResource(
                "/imagens/pesquisar.png"
            )
        );


        // =====================================================
        // TAMANHO INICIAL DA IMAGEM
        // =====================================================

        Image imagemPesquisarRedimensionada =
            pesquisar.getImage()
                .getScaledInstance(
                    250,
                    100,
                    Image.SCALE_SMOOTH
                );


        btnPesquisar.setIcon(
            new ImageIcon(
                imagemPesquisarRedimensionada
            )
        );


        // =====================================================
        // AÇÃO DO BOTÃO PESQUISAR
        // =====================================================

        btnPesquisar.addActionListener(
            new ActionListener() {

                public void actionPerformed(
                    ActionEvent e
                ) {

                    // Ação do botão pesquisar

                }
            }
        );


        // =====================================================
        // BOTÃO REDIMENSIONÁVEL
        // =====================================================
        //
        // Largura:
        // mínimo = 200
        // preferencial = 300
        // máximo = 500
        //
        // Altura:
        // mínimo = 80
        // preferencial = 125
        // máximo = 180
        //
        // =====================================================

        contentPane.add(
            btnPesquisar,
            "cell 0 6 8 1,width 300:200:500,alignx center,height 125:80:180"
        );
        
                // =====================================================
                // LOCALIZAÇÃO
                // =====================================================
        
                JButton btnNewButton_1 = new JButton(
                    "Gaspar"
                );
                
                        btnNewButton_1.setFont(
                            new Font(
                                "Tahoma",
                                Font.BOLD,
                                36
                            )
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
                                                
                                                                public void actionPerformed(
                                                                    ActionEvent e
                                                                ) {
                                                
                                                                    // Ação da localização
                                                
                                                                }
                                                            }
                                                        );
                                                        
                                                        
                                                                // =====================================================
                                                                // LOCALIZAÇÃO
                                                                // =====================================================
                                                        
                                                                contentPane.add(
                                                                    btnNewButton_1,
                                                                    "cell 0 7,alignx left,aligny bottom"
                                                                );


        // =====================================================
        // REDIMENSIONAR A IMAGEM JUNTO COM O BOTÃO
        // =====================================================

        btnPesquisar.addComponentListener(
            new ComponentAdapter() {

                @Override
                public void componentResized(
                    ComponentEvent e
                ) {

                    int largura =
                        btnPesquisar.getWidth();

                    int altura =
                        btnPesquisar.getHeight();


                    if (largura > 0 && altura > 0) {

                        Image imagemOriginal =
                            pesquisar.getImage();


                        // Mantém uma pequena margem
                        int novaLargura =
                            Math.max(
                                1,
                                largura - 10
                            );

                        int novaAltura =
                            Math.max(
                                1,
                                altura - 10
                            );


                        Image novaImagem =
                            imagemOriginal
                                .getScaledInstance(
                                    novaLargura,
                                    novaAltura,
                                    Image.SCALE_SMOOTH
                                );


                        btnPesquisar.setIcon(
                            new ImageIcon(
                                novaImagem
                            )
                        );
                    }
                }
            }
        );
    }


    // =====================================================
    // GETTER - BOTÃO HOME
    // =====================================================

    public JButton getBtnHome() {

        return btnHome;
    }


    // =====================================================
    // GETTER - BOTÃO PESQUISAR
    // =====================================================

    public JButton getBtnPesquisar() {

        return btnPesquisar;
    }


    // =====================================================
    // PEGA O TEXTO DIGITADO
    // =====================================================

    public String getTextoPesquisa() {

        return textField
            .getText()
            .trim();
    }


    // =====================================================
    // LIMPA O CAMPO DE PESQUISA
    // =====================================================

    public void limparPesquisa() {

        textField.setText("");
    }
}
