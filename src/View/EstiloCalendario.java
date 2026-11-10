package View;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;

import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;

// Cores e fontes dos calendários do sistema: a tela Calendário e o
// campo de data com calendário (usado ao escolher as datas do empréstimo)
public final class EstiloCalendario {

    public static final Color VERDE_ESCURO = new Color(19, 74, 38);   // textos
    public static final Color VERDE_PAINEL = new Color(25, 90, 45);   // fundo do calendário
    public static final Color VERDE_BOTAO  = new Color(114, 219, 145); // dia selecionado
    public static final Color DOURADO      = new Color(240, 196, 80);  // domingos e dias marcados

    private EstiloCalendario() {
    }

    // Deixa o JCalendar com as cores do sistema
    public static void aplicar(JCalendar calendario, int tamanhoFonte) {

        Font fonte = new Font("Tahoma", Font.BOLD, tamanhoFonte);

        calendario.setFont(fonte);
        calendario.setWeekOfYearVisible(false);
        calendario.setForeground(VERDE_ESCURO);
        calendario.setBackground(VERDE_PAINEL);
        calendario.setDecorationBackgroundColor(VERDE_PAINEL);
        calendario.setDecorationBordersVisible(false);
        calendario.setWeekdayForeground(Color.WHITE);
        calendario.setSundayForeground(DOURADO);

        JDayChooser dias = calendario.getDayChooser();
        dias.setDayBordersVisible(false);
        dias.getDayPanel().setBackground(Color.WHITE);

        // fundo dos dias e cor do dia selecionado (a biblioteca não tem setter para isso)
        mudarCorDoDayChooser(dias, "oldDayBackgroundColor", Color.WHITE);
        mudarCorDoDayChooser(dias, "selectedColor", VERDE_BOTAO);

        // mês e ano
        calendario.getMonthChooser().setFont(fonte);
        JComboBox<?> comboMes = (JComboBox<?>) calendario.getMonthChooser().getComboBox();
        comboMes.setFont(fonte);
        comboMes.setBackground(Color.WHITE);
        comboMes.setForeground(VERDE_ESCURO);

        calendario.getYearChooser().setFont(fonte);
        calendario.getYearChooser().setForeground(VERDE_ESCURO);
    }

    // Campo de data: caixa de texto, botão e o calendário que abre ao clicar
    public static void aplicar(JDateChooser campo) {

        aplicar(campo.getJCalendar(), 14);
        campo.getJCalendar().setBorder(BorderFactory.createLineBorder(VERDE_PAINEL, 6));

        JComponent texto = campo.getDateEditor().getUiComponent();
        texto.setFont(new Font("Tahoma", Font.PLAIN, 16));
        texto.setForeground(VERDE_ESCURO);
        texto.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_ESCURO, 2),
                BorderFactory.createEmptyBorder(0, 6, 0, 6)));

        JButton botao = campo.getCalendarButton();
        botao.setBackground(VERDE_BOTAO);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(VERDE_ESCURO, 2));
    }

    private static void mudarCorDoDayChooser(JDayChooser dias, String campo, Color cor) {
        try {
            java.lang.reflect.Field f = JDayChooser.class.getDeclaredField(campo);
            f.setAccessible(true);
            f.set(dias, cor);
        } catch (Exception e) {
            // segue com a cor padrão
        }
    }
}
