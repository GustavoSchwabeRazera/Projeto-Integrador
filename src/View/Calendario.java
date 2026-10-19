package View;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.IDateEvaluator;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

import model.Evento;
import net.miginfocom.swing.MigLayout;

public class Calendario extends JFrame {

    private static final long serialVersionUID = 1L;

    // =====================================================
    // PALETA DE CORES (mesma identidade visual do sistema)
    // =====================================================
    private static final Color VERDE_FUNDO   = new Color(175, 244, 198); // fundo geral
    private static final Color VERDE_ESCURO  = new Color(19, 74, 38);    // textos/título
    private static final Color VERDE_PAINEL  = new Color(25, 90, 45);    // painel do calendário
    private static final Color VERDE_BOTAO   = new Color(114, 219, 145); // dia selecionado
    private static final Color DOURADO       = new Color(240, 196, 80);  // dia com empréstimo
    private static final Color VERMELHO      = new Color(170, 50, 50);   // devoluções

    private static final Locale PT_BR = new Locale("pt", "BR");

    private JPanel contentPane;
    private JButton btnHome;
    private JCalendar calendario;
    private JLabel lblDataSelecionada;
    private DefaultListModel<Object> modeloEventos;
    private JList<Object> listaEventos;
    private JTextField campoNovoEvento;
    private JButton btnAdicionarEvento;
    private JButton btnRemoverEvento;

    private LocalDate dataSelecionada;
    private Map<LocalDate, List<String>> emprestimosPorData = new HashMap<>();
    private Map<LocalDate, List<Evento>> lembretesPorData = new HashMap<>();

    /**
     * Painel com cantos arredondados (igual ao das demais telas)
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
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
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
        setBounds(100, 100, 1300, 721);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBackground(VERDE_FUNDO);
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);
        contentPane.setLayout(new MigLayout("", "[30px][grow 60]30[grow 40][30px]", "[][]20[grow,fill][30px]"));

        // =====================================================
        // BOTÃO HOME
        // =====================================================

        btnHome = new JButton("");
        btnHome.setIcon(new ImageIcon(Calendario.class.getResource("/imagens/casa 1.png")));
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.setFocusPainted(false);
        contentPane.add(btnHome, "cell 0 0,alignx left,aligny top");

        // =====================================================
        // TÍTULO
        // =====================================================

        JLabel lblCalendario = new JLabel("Calendário");
        lblCalendario.setForeground(VERDE_ESCURO);
        lblCalendario.setFont(new Font("Tahoma", Font.BOLD, 34));
        contentPane.add(lblCalendario, "cell 1 1 2 1,alignx center");

        // =====================================================
        // PAINEL DO CALENDÁRIO (esquerda)
        // =====================================================

        RoundedPanel painelCalendario = new RoundedPanel(new MigLayout("insets 25, fill", "[grow]", "[grow]"), 40);
        painelCalendario.setBackground(VERDE_PAINEL);
        contentPane.add(painelCalendario, "cell 1 2,grow");

        calendario = new JCalendar(PT_BR);
        calendario.setWeekOfYearVisible(false);
        estilizarCalendario();
        painelCalendario.add(calendario, "cell 0 0,grow");

        // =====================================================
        // PAINEL DE EVENTOS (direita)
        // =====================================================

        RoundedPanel painelEventos = new RoundedPanel(new MigLayout("insets 25", "[grow]", "[]5[]15[grow]15[]10[]15[]"), 40);
        painelEventos.setBackground(VERDE_PAINEL);
        contentPane.add(painelEventos, "cell 2 2,grow");

        lblDataSelecionada = new JLabel("Selecione uma data");
        lblDataSelecionada.setForeground(Color.WHITE);
        lblDataSelecionada.setFont(new Font("Tahoma", Font.BOLD, 22));
        painelEventos.add(lblDataSelecionada, "cell 0 0");

        JLabel lblSubtitulo = new JLabel("Empréstimos e lembretes do dia");
        lblSubtitulo.setForeground(VERDE_FUNDO);
        lblSubtitulo.setFont(new Font("Tahoma", Font.PLAIN, 15));
        painelEventos.add(lblSubtitulo, "cell 0 1");

        modeloEventos = new DefaultListModel<>();
        listaEventos = new JList<>(modeloEventos);
        listaEventos.setFont(new Font("Tahoma", Font.PLAIN, 16));
        listaEventos.setBackground(Color.WHITE);
        listaEventos.setCellRenderer(new RenderizadorEvento());

        JScrollPane scrollEventos = new JScrollPane(listaEventos);
        scrollEventos.setBorder(BorderFactory.createEmptyBorder());
        painelEventos.add(scrollEventos, "cell 0 2,grow");

        campoNovoEvento = new JTextField();
        campoNovoEvento.setFont(new Font("Tahoma", Font.PLAIN, 16));
        painelEventos.add(campoNovoEvento, "flowx,cell 0 3,growx,h 36!");

        btnAdicionarEvento = new JButton("Adicionar lembrete");
        estilizarBotao(btnAdicionarEvento, VERDE_BOTAO, Color.BLACK);
        painelEventos.add(btnAdicionarEvento, "cell 0 3,h 36!");

        btnRemoverEvento = new JButton("Remover lembrete selecionado");
        estilizarBotao(btnRemoverEvento, new Color(200, 60, 60), Color.WHITE);
        painelEventos.add(btnRemoverEvento, "cell 0 4,alignx right,h 36!");

        JLabel lblLegenda = new JLabel("  Dias marcados têm empréstimo ou lembrete");
        lblLegenda.setOpaque(false);
        lblLegenda.setForeground(Color.WHITE);
        lblLegenda.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblLegenda.setIcon(new QuadradoCor(DOURADO));
        painelEventos.add(lblLegenda, "cell 0 5");

        // =====================================================
        // AÇÕES
        // =====================================================

        calendario.addPropertyChangeListener("calendar", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                selecionarData(converterParaLocalDate(calendario.getDate()));
                redesenharDias();
            }
        });

        selecionarData(LocalDate.now());
    }

    // =====================================================
    // DADOS VINDOS DO BANCO
    // =====================================================

    public void mostrarEventos(Map<LocalDate, List<String>> emprestimos, Map<LocalDate, List<Evento>> lembretes) {
        emprestimosPorData = emprestimos;
        lembretesPorData = lembretes;

        // atualiza a lista do dia e repinta os dias marcados
        selecionarData(dataSelecionada);
        redesenharDias();
    }

    // Ao trocar de dia o JCalendar apaga a cor do dia anterior,
    // então redesenha o mês para os dias marcados continuarem dourados
    private void redesenharDias() {
        calendario.getDayChooser().setMonth(calendario.getMonthChooser().getMonth());
    }

    private void selecionarData(LocalDate data) {
        dataSelecionada = data;

        String nomeDia = data.getDayOfWeek().getDisplayName(TextStyle.FULL, PT_BR);
        String nomeMes = data.getMonth().getDisplayName(TextStyle.FULL, PT_BR);
        lblDataSelecionada.setText(data.getDayOfMonth() + " de " + nomeMes + " (" + nomeDia + ")");

        modeloEventos.clear();

        List<String> emprestimos = emprestimosPorData.get(data);
        if (emprestimos != null) {
            for (String emprestimo : emprestimos) {
                modeloEventos.addElement(emprestimo);
            }
        }

        List<Evento> lembretes = lembretesPorData.get(data);
        if (lembretes != null) {
            for (Evento lembrete : lembretes) {
                modeloEventos.addElement(lembrete);
            }
        }

        if (modeloEventos.isEmpty()) {
            modeloEventos.addElement("Nada marcado neste dia.");
        }
    }

    private LocalDate converterParaLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // =====================================================
    // ESTILO DO JCALENDAR
    // =====================================================

    private void estilizarCalendario() {

        calendario.setFont(new Font("Tahoma", Font.BOLD, 18));
        calendario.setForeground(VERDE_ESCURO);
        calendario.setBackground(VERDE_PAINEL);
        calendario.setDecorationBackgroundColor(VERDE_PAINEL);
        calendario.setDecorationBordersVisible(false);
        calendario.setWeekdayForeground(Color.WHITE);
        calendario.setSundayForeground(DOURADO);

        JDayChooser dias = calendario.getDayChooser();
        dias.setDayBordersVisible(false);
        dias.getDayPanel().setBackground(Color.WHITE);
        dias.setAlwaysFireDayProperty(true);

        // fundo dos dias e cor do dia selecionado (a biblioteca não tem setter para isso)
        mudarCorDoDayChooser(dias, "oldDayBackgroundColor", Color.WHITE);
        mudarCorDoDayChooser(dias, "selectedColor", VERDE_BOTAO);

        // marca em dourado os dias com empréstimo/devolução
        dias.addDateEvaluator(new IDateEvaluator() {
            public boolean isSpecial(Date date) {
                LocalDate data = converterParaLocalDate(date);
                return emprestimosPorData.containsKey(data) || lembretesPorData.containsKey(data);
            }
            public Color getSpecialForegroundColor() { return VERDE_ESCURO; }
            public Color getSpecialBackroundColor() { return DOURADO; }
            public String getSpecialTooltip() { return "Empréstimo ou lembrete neste dia"; }
            public boolean isInvalid(Date date) { return false; }
            public Color getInvalidForegroundColor() { return null; }
            public Color getInvalidBackroundColor() { return null; }
            public String getInvalidTooltip() { return null; }
        });

        // mês e ano
        calendario.getMonthChooser().setFont(new Font("Tahoma", Font.BOLD, 18));
        JComboBox<?> comboMes = (JComboBox<?>) calendario.getMonthChooser().getComboBox();
        comboMes.setFont(new Font("Tahoma", Font.BOLD, 18));
        comboMes.setBackground(Color.WHITE);
        comboMes.setForeground(VERDE_ESCURO);

        calendario.getYearChooser().setFont(new Font("Tahoma", Font.BOLD, 18));
        calendario.getYearChooser().setForeground(VERDE_ESCURO);
    }

    private void estilizarBotao(JButton botao, Color fundo, Color texto) {
        botao.setFont(new Font("Tahoma", Font.BOLD, 15));
        botao.setBackground(fundo);
        botao.setForeground(texto);
        botao.setFocusPainted(false);
        botao.setBorderPainted(false);
    }

    private void mudarCorDoDayChooser(JDayChooser dias, String campo, Color cor) {
        try {
            java.lang.reflect.Field f = JDayChooser.class.getDeclaredField(campo);
            f.setAccessible(true);
            f.set(dias, cor);
        } catch (Exception e) {
            // segue com a cor padrão
        }
    }

    // Deixa cada linha da lista com espaço e cor conforme o tipo
    private static class RenderizadorEvento extends DefaultListCellRenderer {

        private static final long serialVersionUID = 1L;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, index, false, false);
            String texto = String.valueOf(value);

            lbl.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, VERDE_FUNDO),
                    new EmptyBorder(12, 14, 12, 14)));

            if (value instanceof Evento) {
                lbl.setText("• " + texto);
                lbl.setForeground(VERDE_ESCURO);
                lbl.setFont(lbl.getFont().deriveFont(Font.ITALIC));
                if (isSelected) {
                    lbl.setBackground(VERDE_FUNDO);
                }
            } else if (texto.endsWith("(aguardando)") || texto.endsWith("(aguardando você)")) {
                lbl.setForeground(new Color(160, 110, 0));
            } else if (texto.startsWith("Devolver") || texto.startsWith("Receber")) {
                lbl.setForeground(VERMELHO);
                lbl.setFont(lbl.getFont().deriveFont(Font.BOLD));
            } else if (texto.startsWith("Nenhum")) {
                lbl.setForeground(Color.GRAY);
            } else {
                lbl.setForeground(VERDE_ESCURO);
            }

            return lbl;
        }
    }

    // Quadradinho colorido usado na legenda
    private static class QuadradoCor implements javax.swing.Icon {

        private final Color cor;

        QuadradoCor(Color cor) {
            this.cor = cor;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(cor);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
        }

        public int getIconWidth() { return 16; }

        public int getIconHeight() { return 16; }
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

    public JTextField getCampoNovoEvento() {
        return campoNovoEvento;
    }

    public JButton getBtnAdicionarEvento() {
        return btnAdicionarEvento;
    }

    public JButton getBtnRemoverEvento() {
        return btnRemoverEvento;
    }

    // Lembrete selecionado na lista (null se for um empréstimo ou nada)
    public Evento getEventoSelecionado() {
        Object item = listaEventos.getSelectedValue();
        return (item instanceof Evento) ? (Evento) item : null;
    }
}
