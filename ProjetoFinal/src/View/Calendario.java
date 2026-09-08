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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
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

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.IDateEvaluator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Date;
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
    private static final Color DOURADO_EVENTO    = new Color(230, 178, 44); // marcador de evento

    private JPanel contentPane;
    private JButton btnHome;

    private JCalendar calendario;

    private JLabel lblDataSelecionada;
    private DefaultListModel<String> modeloEventos;
    private JList<String> listaEventos;
    private JTextField campoNovoEvento;
    private JButton btnAdicionarEvento;
    private JButton btnRemoverEvento;

    private LocalDate dataSelecionada;
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
        // PAINEL DO CALENDÁRIO (esquerda) - agora usando JCalendar
        // =====================================================

        RoundedPanel painelCalendario = new RoundedPanel(
            new MigLayout(
                "insets 25 30 25 30, fill",
                "[grow]",
                "[grow]"
            ),
            40
        );

        painelCalendario.setBackground(VERDE_PAINEL);

        contentPane.add(
            painelCalendario,
            "cell 0 4 3 8,grow"
        );

        calendario = new JCalendar(new Locale("pt", "BR"));
        calendario.setWeekOfYearVisible(false);
        estilizarCalendario(calendario);

        // marca com uma bolinha dourada os dias que já têm evento cadastrado
        calendario.getDayChooser().addDateEvaluator(new IDateEvaluator() {

            @Override
            public boolean isSpecial(Date date) {
                LocalDate data = converterParaLocalDate(date);
                List<String> eventos = eventosPorData.get(data);
                return eventos != null && !eventos.isEmpty();
            }

            @Override
            public Color getSpecialForegroundColor() {
                return VERDE_ESCURO;
            }

            @Override
            public Color getSpecialBackroundColor() {
                return DOURADO_EVENTO;
            }

            @Override
            public String getSpecialTooltip() {
                return "Há eventos nesse dia";
            }

            @Override
            public boolean isInvalid(Date date) {
                return false;
            }

            @Override
            public Color getInvalidForegroundColor() {
                return null;
            }

            @Override
            public Color getInvalidBackroundColor() {
                return null;
            }

            @Override
            public String getInvalidTooltip() {
                return null;
            }
        });

        painelCalendario.add(calendario, "cell 0 0,grow");

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

        // dispara sempre que o usuário troca o dia (ou navega de mês/ano e clica num dia)
        calendario.addPropertyChangeListener("date", new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Date novaData = calendario.getDate();
                if (novaData != null) {
                    selecionarData(converterParaLocalDate(novaData));
                }
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

        // seleciona hoje já na abertura da tela
        selecionarData(LocalDate.now());
    }

    // =====================================================
    // CONVERSÃO Date <-> LocalDate
    // =====================================================
    private LocalDate converterParaLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
            .atZone(ZoneId.systemDefault())
            .toLocalDate();
    }

    // =====================================================
    // SELEÇÃO DE DATA
    // =====================================================
    private void selecionarData(LocalDate data) {

        dataSelecionada = data;

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
        // repinta a grade do JCalendar pra mostrar/atualizar a bolinha dourada do dia
        calendario.getDayChooser().repaint();
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
        calendario.getDayChooser().repaint();
    }

    // =====================================================
    // ESTILOS AUXILIARES
    // =====================================================

    /**
     * Aplica a paleta de cores do sistema (verde escuro/dourado) ao JCalendar.
     * A API da lib expõe estilização de: fundo/fonte geral (propaga pros
     * componentes internos), linha de cabeçalho dos dias da semana e as
     * cores do texto de domingo x dias úteis. O combo de mês e o campo de
     * ano também são pegos manualmente para ficarem no mesmo tom.
     */
    private void estilizarCalendario(JCalendar calendario) {

        calendario.setBackground(Color.WHITE);
        calendario.setForeground(VERDE_ESCURO);
        calendario.setFont(new Font("Tahoma", Font.PLAIN, 14));

        // linha "Dom Seg Ter Qua Qui Sex Sáb"
        calendario.setDecorationBackgroundColor(VERDE_PAINEL);
        calendario.setWeekdayForeground(Color.WHITE);
        calendario.setSundayForeground(DOURADO_EVENTO);

        calendario.getDayChooser().setBackground(Color.WHITE);

        // combo de mês (ex: "Janeiro")
        Object comboMes = calendario.getMonthChooser().getComboBox();
        if (comboMes instanceof JComboBox) {
            JComboBox<?> combo = (JComboBox<?>) comboMes;
            combo.setBackground(Color.WHITE);
            combo.setForeground(VERDE_ESCURO);
            combo.setFont(new Font("Tahoma", Font.BOLD, 14));
        }

        // campo/spinner de ano
        calendario.getYearChooser().setBackground(Color.WHITE);
        calendario.getYearChooser().setForeground(VERDE_ESCURO);
        calendario.getYearChooser().setFont(new Font("Tahoma", Font.BOLD, 14));
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

    public JCalendar getCalendario() {
        return calendario;
    }

    public LocalDate getDataSelecionada() {
        return dataSelecionada;
    }

    public Map<LocalDate, List<String>> getEventosPorData() {
        return eventosPorData;
    }
}