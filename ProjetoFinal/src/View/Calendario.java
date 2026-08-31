package View;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.LayoutManager;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;

import net.miginfocom.swing.MigLayout;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Calendario extends JFrame {

    private static final long serialVersionUID = 1L;

    // =====================================================
    // PALETA DE CORES (mesma identidade visual do sistema)
    // =====================================================
    private static final Color VERDE_FUNDO      = new Color(175, 244, 198); // fundo geral
    private static final Color VERDE_ESCURO      = new Color(10, 86, 27);   // textos/título
    private static final Color VERDE_PAINEL      = new Color(36, 107, 45);  // painel principal
    private static final Color VERDE_CARD        = new Color(174, 244, 198); // cards internos
    private static final Color VERDE_HOJE        = new Color(255, 255, 255); // destaque "hoje"
    private static final Color VERDE_SELECIONADO = new Color(10, 86, 27);   // dia selecionado
    private static final Color DOURADO_EVENTO    = new Color(230, 178, 44); // marcador de evento

    private JPanel contentPane;
    private JButton btnHome;

    private JButton btnMesAnterior;
    private JButton btnMesProximo;
    private JLabel lblMesAno;
    private JPanel painelDias;

    private JLabel lblDataSelecionada;
    private DefaultListModel<String> modeloEventos;
    private JList<String> listaEventos;
    private JTextField campoNovoEvento;
    private JButton btnAdicionarEvento;
    private JButton btnRemoverEvento;

    private YearMonth mesAtual;
    private LocalDate dataSelecionada;
    private JButton botaoDiaSelecionado;
    private final Map<LocalDate, List<String>> eventosPorData = new LinkedHashMap<>();

    /**
     * Painel com cantos arredondados (idêntico ao usado nas demais telas)
     */
    private static class RoundedPanel extends JPanel {

        private static final long serialVersionUID = 1L;
        private final int radius;

        public RoundedPanel(LayoutManager layout, int radius) {
            super(layout);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g2.fillRoundRect(
                0,
                0,
                getWidth() - 1,
                getHeight() - 1,
                radius,
                radius
            );

            g2.dispose();

            super.paintComponent(g);
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {

                    Calendario frame = new Calendario();
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
    public Calendario() {

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setBounds(100, 100, 1920, 1080);

        mesAtual = YearMonth.now();

        contentPane = new JPanel();
        contentPane.setBackground(VERDE_FUNDO);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);

        contentPane.setLayout(
            new MigLayout("", "[200,grow][200,grow][200,grow][200,grow][200,grow]", "[][113.00,grow][][][grow][grow][grow][grow][grow][grow][grow][grow]")
        );

        // =====================================================
        // BOTÃO HOME
        // =====================================================

        btnHome = new JButton("");
        btnHome.setPreferredSize(new Dimension(50, 50));

        try {
            ImageIcon iconeHome = new ImageIcon(
                Calendario.class.getResource("/imagens/casa 1.png")
            );
            Image imagemHome = iconeHome.getImage().getScaledInstance(
                60, 60, Image.SCALE_SMOOTH
            );
            btnHome.setIcon(new ImageIcon(imagemHome));
        } catch (Exception e) {
            btnHome.setText("Home");
        }

        btnHome.setFont(new Font("Tahoma", Font.PLAIN, 28));
        btnHome.setForeground(VERDE_ESCURO);
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);

        contentPane.add(
            btnHome,
            "cell 0 0,alignx left,aligny top"
        );

        // =====================================================
        // LOGO
        // =====================================================

        JLabel lblNewLabel = new JLabel("");

        try {
            lblNewLabel.setIcon(
                new ImageIcon(
                    Calendario.class.getResource("/imagens/Logo.png")
                )
            );
        } catch (Exception e) {
            // segue sem logo caso o recurso não exista
        }

        contentPane.add(
            lblNewLabel,
            "cell 0 1 5 1,alignx center"
        );

        
        
                // =====================================================
                // TÍTULO
                // =====================================================
        
                JLabel lblCalendario = new JLabel("Calendário");
                
                        lblCalendario.setForeground(VERDE_ESCURO);
                        lblCalendario.setFont(new Font("Tahoma", Font.BOLD, 34));
                        
                                contentPane.add(
                                    lblCalendario,
                                    "cell 2 2,alignx center"
                                );

        // =====================================================
        // PAINEL DO CALENDÁRIO (esquerda)
        // =====================================================

        RoundedPanel painelCalendario = new RoundedPanel(
            new MigLayout(
                "insets 25 30 25 30",
                "[grow]",
                "[]15[]15[grow]"
            ),
            40
        );

        painelCalendario.setBackground(VERDE_PAINEL);

        contentPane.add(
            painelCalendario,
            "cell 0 4 3 8,grow"
        );

        // ---- navegação de mês ----

        JPanel painelNavegacao = new JPanel(
            new MigLayout(
                "insets 0",
                "[]push[grow,center]push[]",
                "[]"
            )
        );
        painelNavegacao.setOpaque(false);

        btnMesAnterior = new JButton("<");
        estilizarBotaoNavegacao(btnMesAnterior);

        lblMesAno = new JLabel("", javax.swing.SwingConstants.CENTER);
        lblMesAno.setForeground(Color.WHITE);
        lblMesAno.setFont(new Font("Tahoma", Font.BOLD, 24));

        btnMesProximo = new JButton(">");
        estilizarBotaoNavegacao(btnMesProximo);

        painelNavegacao.add(btnMesAnterior, "cell 0 0");
        painelNavegacao.add(lblMesAno, "cell 1 0,growx");
        painelNavegacao.add(btnMesProximo, "cell 2 0");

        painelCalendario.add(painelNavegacao, "cell 0 0,growx");

        // ---- cabeçalho dos dias da semana ----

        JPanel painelCabecalhoSemana = new JPanel(new GridLayout(1, 7, 6, 6));
        painelCabecalhoSemana.setOpaque(false);

        String[] diasSemana = {"Dom", "Seg", "Ter", "Qua", "Qui", "Sex", "Sáb"};
        for (String dia : diasSemana) {
            JLabel lblDia = new JLabel(dia, javax.swing.SwingConstants.CENTER);
            lblDia.setForeground(Color.WHITE);
            lblDia.setFont(new Font("Tahoma", Font.BOLD, 14));
            painelCabecalhoSemana.add(lblDia);
        }

        painelCalendario.add(painelCabecalhoSemana, "cell 0 1,growx");

        // ---- grade de dias ----

        painelDias = new JPanel(new GridLayout(6, 7, 6, 6));
        painelDias.setOpaque(false);

        painelCalendario.add(painelDias, "cell 0 2,grow");

        // =====================================================
        // PAINEL DE EVENTOS (direita)
        // =====================================================

        RoundedPanel painelEventos = new RoundedPanel(
            new MigLayout(
                "insets 25 25 25 25",
                "[grow]",
                "[]15[grow]15[]10[]"
            ),
            40
        );

        painelEventos.setBackground(VERDE_CARD);

        contentPane.add(
            painelEventos,
            "cell 3 4 2 8,grow"
        );

        lblDataSelecionada = new JLabel("Selecione uma data");
        lblDataSelecionada.setForeground(VERDE_ESCURO);
        lblDataSelecionada.setFont(new Font("Tahoma", Font.BOLD, 19));

        painelEventos.add(lblDataSelecionada, "cell 0 0,growx");

        modeloEventos = new DefaultListModel<>();
        listaEventos = new JList<>(modeloEventos);
        listaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaEventos.setFont(new Font("Tahoma", Font.PLAIN, 15));
        listaEventos.setBackground(Color.WHITE);

        JScrollPane scrollEventos = new JScrollPane(listaEventos);
        scrollEventos.setBorder(BorderFactory.createLineBorder(VERDE_PAINEL, 1));

        painelEventos.add(scrollEventos, "cell 0 1,grow");

        JPanel painelAdicionar = new JPanel(
            new MigLayout(
                "insets 0",
                "[grow][]",
                "[]"
            )
        );
        painelAdicionar.setOpaque(false);

        campoNovoEvento = new JTextField();
        campoNovoEvento.setFont(new Font("Tahoma", Font.PLAIN, 15));

        btnAdicionarEvento = new JButton("Adicionar");
        estilizarBotaoAcao(btnAdicionarEvento, VERDE_ESCURO);

        painelAdicionar.add(campoNovoEvento, "cell 0 0,growx");
        painelAdicionar.add(btnAdicionarEvento, "cell 1 0");

        painelEventos.add(painelAdicionar, "cell 0 2,growx");

        btnRemoverEvento = new JButton("Remover evento selecionado");
        estilizarBotaoAcao(btnRemoverEvento, new Color(150, 40, 40));

        painelEventos.add(btnRemoverEvento, "cell 0 4,alignx center");

        // =====================================================
        // AÇÕES
        // =====================================================

        btnMesAnterior.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mesAtual = mesAtual.minusMonths(1);
                atualizarGradeCalendario();
            }
        });

        btnMesProximo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mesAtual = mesAtual.plusMonths(1);
                atualizarGradeCalendario();
            }
        });

        btnAdicionarEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarEvento();
            }
        });

        campoNovoEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarEvento();
            }
        });

        btnRemoverEvento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removerEventoSelecionado();
            }
        });

        atualizarGradeCalendario();
    }

    // =====================================================
    // MONTA A GRADE DE DIAS DO MÊS ATUAL
    // =====================================================
    private void atualizarGradeCalendario() {

        painelDias.removeAll();
        botaoDiaSelecionado = null;

        String nomeMes = mesAtual.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
        nomeMes = nomeMes.substring(0, 1).toUpperCase() + nomeMes.substring(1);
        lblMesAno.setText(nomeMes + " de " + mesAtual.getYear());

        LocalDate primeiroDia = mesAtual.atDay(1);
        int offset = primeiroDia.getDayOfWeek().getValue() % 7; // Domingo = 0
        int diasNoMes = mesAtual.lengthOfMonth();

        for (int i = 0; i < offset; i++) {
            JLabel vazio = new JLabel("");
            painelDias.add(vazio);
        }

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate data = mesAtual.atDay(dia);
            painelDias.add(criarBotaoDia(data));
        }

        int totalCelulas = offset + diasNoMes;
        int restante = (7 - (totalCelulas % 7)) % 7;
        for (int i = 0; i < restante; i++) {
            painelDias.add(new JLabel(""));
        }

        painelDias.revalidate();
        painelDias.repaint();
    }

    // =====================================================
    // CRIA UM BOTÃO DE DIA COM VISUAL CONSISTENTE
    // =====================================================
    private JButton criarBotaoDia(final LocalDate data) {

        JButton botao = new JButton(String.valueOf(data.getDayOfMonth()));
        botao.setFont(new Font("Tahoma", Font.PLAIN, 15));
        botao.setFocusPainted(false);
        botao.setOpaque(true);
        botao.setBorderPainted(true);

        boolean temEvento = eventosPorData.containsKey(data) && !eventosPorData.get(data).isEmpty();
        boolean ehHoje = data.equals(LocalDate.now());

        if (ehHoje) {
            botao.setBackground(VERDE_HOJE);
            botao.setForeground(VERDE_ESCURO);
            botao.setBorder(BorderFactory.createLineBorder(VERDE_ESCURO, 2));
        } else {
            botao.setBackground(VERDE_CARD);
            botao.setForeground(VERDE_ESCURO);
            botao.setBorder(BorderFactory.createLineBorder(VERDE_PAINEL, 1));
        }

        if (temEvento) {
            botao.setText("<html><center>" + data.getDayOfMonth() + "<br><font color='#E6B22C'>&#9679;</font></center></html>");
        }

        botao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selecionarData(data, botao);
            }
        });

        return botao;
    }

    // =====================================================
    // SELEÇÃO DE DATA
    // =====================================================
    private void selecionarData(LocalDate data, JButton botaoClicado) {

        dataSelecionada = data;

        if (botaoDiaSelecionado != null) {
            botaoDiaSelecionado.setBorder(BorderFactory.createLineBorder(VERDE_PAINEL, 1));
        }

        botaoClicado.setBorder(BorderFactory.createLineBorder(VERDE_SELECIONADO, 3));
        botaoDiaSelecionado = botaoClicado;

        String[] diasSemana = {"domingo", "segunda-feira", "terça-feira", "quarta-feira", "quinta-feira", "sexta-feira", "sábado"};
        DayOfWeek dow = data.getDayOfWeek();
        String nomeDia = diasSemana[dow.getValue() % 7];

        lblDataSelecionada.setText(
            data.getDayOfMonth() + " de " +
            capitalizar(data.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"))) +
            " (" + capitalizar(nomeDia) + ")"
        );

        atualizarListaEventos();
    }

    private String capitalizar(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        return texto.substring(0, 1).toUpperCase() + texto.substring(1);
    }

    // =====================================================
    // EVENTOS
    // =====================================================
    private void atualizarListaEventos() {
        modeloEventos.clear();
        if (dataSelecionada == null) return;

        List<String> eventos = eventosPorData.get(dataSelecionada);
        if (eventos != null) {
            for (String evento : eventos) {
                modeloEventos.addElement(evento);
            }
        }
    }

    private void adicionarEvento() {

        if (dataSelecionada == null) {
            lblDataSelecionada.setText("Selecione uma data primeiro");
            return;
        }

        String texto = campoNovoEvento.getText().trim();
        if (texto.isEmpty()) return;

        eventosPorData.computeIfAbsent(dataSelecionada, k -> new ArrayList<>()).add(texto);
        campoNovoEvento.setText("");

        atualizarListaEventos();
        atualizarGradeCalendario();
        // reseleciona visualmente a data após redesenhar a grade
        realcarBotaoDaDataSelecionada();
    }

    private void removerEventoSelecionado() {

        if (dataSelecionada == null) return;

        int indice = listaEventos.getSelectedIndex();
        if (indice == -1) return;

        List<String> eventos = eventosPorData.get(dataSelecionada);
        if (eventos != null && indice < eventos.size()) {
            eventos.remove(indice);
            if (eventos.isEmpty()) {
                eventosPorData.remove(dataSelecionada);
            }
        }

        atualizarListaEventos();
        atualizarGradeCalendario();
        realcarBotaoDaDataSelecionada();
    }

    // Depois de redesenhar a grade, reencontra e realça o botão do dia selecionado
    private void realcarBotaoDaDataSelecionada() {
        if (dataSelecionada == null) return;

        for (java.awt.Component c : painelDias.getComponents()) {
            if (c instanceof JButton) {
                JButton b = (JButton) c;
                String texto = ((JButton) c).getText().replaceAll("<[^>]*>", "").trim();
                if (texto.equals(String.valueOf(dataSelecionada.getDayOfMonth()))
                        && mesAtual.equals(YearMonth.from(dataSelecionada))) {
                    b.setBorder(BorderFactory.createLineBorder(VERDE_SELECIONADO, 3));
                    botaoDiaSelecionado = b;
                    break;
                }
            }
        }
    }

    // =====================================================
    // ESTILOS AUXILIARES
    // =====================================================
    private void estilizarBotaoNavegacao(JButton botao) {
        botao.setFont(new Font("Tahoma", Font.BOLD, 22));
        botao.setForeground(Color.WHITE);
        botao.setBorderPainted(false);
        botao.setContentAreaFilled(false);
        botao.setFocusPainted(false);
        botao.setPreferredSize(new Dimension(40, 40));
    }

    private void estilizarBotaoAcao(JButton botao, Color cor) {
        botao.setFont(new Font("Tahoma", Font.BOLD, 14));
        botao.setForeground(Color.WHITE);
        botao.setBackground(cor);
        botao.setOpaque(true);
        botao.setBorderPainted(false);
        botao.setFocusPainted(false);
    }

    // =====================================================
    // GETTERS
    // =====================================================
    public JButton getBtnHome() {
        return btnHome;
    }

   

    public LocalDate getDataSelecionada() {
        return dataSelecionada;
    }

    public Map<LocalDate, List<String>> getEventosPorData() {
        return eventosPorData;
    }
}