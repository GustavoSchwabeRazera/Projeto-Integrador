package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.SecondaryLoop;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;

import com.toedter.calendar.JDateChooser;

// Janela de mensagem do sistema (sucesso, erro, aviso e confirmação).
// Os métodos só retornam depois que o usuário responde, e as outras telas
// ficam bloqueadas enquanto ela está aberta.
public class JanelaMensagem extends JFrame {

    private static final long serialVersionUID = 1L;

    private static final Color VERDE_FUNDO = Cores.FUNDO;
    private static final Color VERDE_ESCURO = new Color(10, 86, 27);
    private static final Color VERDE_BOTAO = new Color(114, 219, 145);
    private static final Color VERMELHO = new Color(140, 20, 20);

    private static final int LARGURA_TEXTO = 440;

    // Botão clicado (0, 1, ...) ou -1 se fechou no X
    private int escolha = -1;

    private SecondaryLoop espera;

    private final JButton botaoPadrao;

    // Telas bloqueadas enquanto a mensagem está aberta
    private final List<Window> bloqueadas = new ArrayList<>();

    // Tela que estava em uso quando a mensagem abriu
    private Window anterior;


    private JanelaMensagem(
            Object conteudo,
            String titulo,
            Color corTitulo,
            Icon icone,
            int indicePadrao,
            String... opcoes) {

        super(titulo);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        setAlwaysOnTop(true);

        JPanel fundo = new JPanel(new BorderLayout(15, 15));
        fundo.setBackground(VERDE_FUNDO);
        fundo.setBorder(new EmptyBorder(20, 25, 20, 25));
        setContentPane(fundo);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setForeground(corTitulo);
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 22));
        fundo.add(lblTitulo, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(15, 0));
        centro.setOpaque(false);

        if (icone != null) {
            centro.add(new JLabel(icone), BorderLayout.WEST);
        }

        centro.add(criarConteudo(conteudo), BorderLayout.CENTER);
        fundo.add(centro, BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);

        JButton padrao = null;

        for (int i = 0; i < opcoes.length; i++) {

            JButton botao = criarBotao(opcoes[i], i);
            painelBotoes.add(botao);

            if (i == indicePadrao) {
                padrao = botao;
            }
        }

        botaoPadrao = padrao;
        fundo.add(painelBotoes, BorderLayout.SOUTH);

        getRootPane().setDefaultButton(botaoPadrao);

        // ESC fecha a janela
        getRootPane().registerKeyboardAction(
                e -> fechar(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );

        pack();
        setLocationRelativeTo(null);
    }


    private JComponent criarConteudo(Object conteudo) {

        if (conteudo instanceof JComponent) {
            return (JComponent) conteudo;
        }

        JTextArea texto = new JTextArea(String.valueOf(conteudo));
        texto.setFont(new Font("Tahoma", Font.PLAIN, 16));
        texto.setForeground(new Color(30, 30, 30));
        texto.setOpaque(false);
        texto.setEditable(false);
        texto.setFocusable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);

        // Define a largura para calcular a altura com as linhas quebradas
        texto.setSize(new Dimension(LARGURA_TEXTO, Short.MAX_VALUE));
        texto.setPreferredSize(
                new Dimension(LARGURA_TEXTO, texto.getPreferredSize().height)
        );

        return texto;
    }


    private JButton criarBotao(String texto, int indice) {

        JButton botao = new TelaMeusLivros.BotaoArredondado(texto, 40);
        botao.setBackground(VERDE_BOTAO);
        botao.setForeground(Color.BLACK);
        botao.setFont(new Font("Tahoma", Font.BOLD, 15));
        botao.setMargin(new Insets(6, 18, 6, 18));

        botao.addActionListener(e -> {
            escolha = indice;
            fechar();
        });

        return botao;
    }


    // Abre a janela e espera a resposta sem travar a pintura das outras telas
    private int abrir() {

        anterior =
                KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();

        for (Window janela : Window.getWindows()) {

            if (janela != this && janela.isVisible() && janela.isEnabled()) {
                janela.setEnabled(false);
                bloqueadas.add(janela);
            }
        }

        espera = Toolkit.getDefaultToolkit()
                .getSystemEventQueue()
                .createSecondaryLoop();

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                fechar();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                espera.exit();
            }
        });

        setVisible(true);

        if (botaoPadrao != null) {
            botaoPadrao.requestFocusInWindow();
        }

        espera.enter();

        return escolha;
    }


    // Reativa as telas antes de fechar: se todas estiverem bloqueadas na hora
    // em que a mensagem some, o Windows passa o foco para outro programa
    private void fechar() {

        for (Window janela : bloqueadas) {
            janela.setEnabled(true);
        }

        if (anterior != null && anterior.isVisible()) {
            anterior.toFront();
        }

        dispose();
    }


    // =========================================================
    // MENSAGEM SIMPLES (BOTÃO OK)
    // =========================================================

    public static void mostrar(String texto) {

        boolean erro = texto.startsWith("Erro");

        new JanelaMensagem(
                texto,
                tituloDa(texto),
                erro ? VERMELHO : VERDE_ESCURO,
                null,
                0,
                "OK"
        ).abrir();
    }


    // O título vem do conteúdo da mensagem
    private static String tituloDa(String texto) {

        if (texto.startsWith("Erro")) {
            return "Erro";
        }

        String primeiraLinha = texto.split("\n")[0];

        if (texto.toLowerCase().contains("sucesso") || primeiraLinha.contains("!")) {
            return "Sucesso";
        }

        return "Aviso";
    }


    // =========================================================
    // PERGUNTA SIM / NÃO
    // =========================================================

    public static boolean confirmar(String pergunta, String titulo) {

        return new JanelaMensagem(
                pergunta,
                titulo,
                VERDE_ESCURO,
                null,
                1,
                "Sim", "Não"
        ).abrir() == 0;
    }


    // =========================================================
    // VÁRIOS BOTÕES
    // Devolve o número do botão clicado (0, 1, ...) ou -1 se fechou no X
    // =========================================================

    public static int escolher(Object conteudo, String titulo, Icon icone, String... opcoes) {

        return new JanelaMensagem(
                conteudo,
                titulo,
                VERDE_ESCURO,
                icone,
                opcoes.length - 1,
                opcoes
        ).abrir();
    }


    // =========================================================
    // PEDE UM TEXTO
    // Devolve o texto digitado ou null se cancelou
    // =========================================================

    public static String pedirTexto(String pergunta, String titulo, String valorInicial) {

        JTextField campo = new JTextField(valorInicial);
        campo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        campo.setPreferredSize(new Dimension(LARGURA_TEXTO, 36));
        campo.setBorder(BorderFactory.createLineBorder(VERDE_ESCURO, 2));

        JLabel lblPergunta = new JLabel(pergunta);
        lblPergunta.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JPanel painel = new JPanel(new BorderLayout(0, 8));
        painel.setOpaque(false);
        painel.add(lblPergunta, BorderLayout.NORTH);
        painel.add(campo, BorderLayout.CENTER);

        JanelaMensagem janela = new JanelaMensagem(
                painel,
                titulo,
                VERDE_ESCURO,
                null,
                0,
                "Salvar", "Cancelar"
        );

        EventQueue.invokeLater(campo::requestFocusInWindow);

        return janela.abrir() == 0 ? campo.getText() : null;
    }


    // =========================================================
    // PEDE O DIA DE RETIRADA E O DIA DE DEVOLUÇÃO
    // Devolve { retirada, devolução } ou null se cancelou.
    // Uma data digitada errada volta como null dentro do vetor.
    // =========================================================

    public static LocalDate[] pedirPeriodo(
            String pergunta,
            String titulo,
            LocalDate retiradaInicial,
            LocalDate retornoInicial,
            LocalDate primeiraRetirada,
            LocalDate ultimaRetirada,
            int maxDiasEmprestimo) {

        JDateChooser campoRetirada = criarCampoData(retiradaInicial);
        campoRetirada.setMinSelectableDate(paraDate(primeiraRetirada));
        campoRetirada.setMaxSelectableDate(paraDate(ultimaRetirada));

        JDateChooser campoRetorno = criarCampoData(retornoInicial);

        // A devolução só pode ser depois da retirada e dentro do prazo máximo
        Runnable limitarRetorno = () -> {
            LocalDate retirada = paraLocalDate(campoRetirada.getDate());
            if (retirada == null) {
                retirada = primeiraRetirada;
            }
            campoRetorno.setMinSelectableDate(paraDate(retirada.plusDays(1)));
            campoRetorno.setMaxSelectableDate(paraDate(retirada.plusDays(maxDiasEmprestimo)));
        };
        limitarRetorno.run();
        campoRetirada.addPropertyChangeListener("date", e -> limitarRetorno.run());

        JLabel lblPergunta = new JLabel("<html><div style='width:" + LARGURA_TEXTO + "px'>"
                + pergunta.replace("\n", "<br>") + "</div></html>");
        lblPergunta.setFont(new Font("Tahoma", Font.PLAIN, 16));

        JPanel campos = new JPanel(new java.awt.GridLayout(2, 2, 10, 10));
        campos.setOpaque(false);
        campos.add(criarRotulo("Dia de retirada:"));
        campos.add(campoRetirada);
        campos.add(criarRotulo("Dia de devolução ao dono:"));
        campos.add(campoRetorno);

        JPanel painel = new JPanel(new BorderLayout(0, 15));
        painel.setOpaque(false);
        painel.add(lblPergunta, BorderLayout.NORTH);
        painel.add(campos, BorderLayout.CENTER);

        JanelaMensagem janela = new JanelaMensagem(
                painel,
                titulo,
                VERDE_ESCURO,
                null,
                0,
                "Enviar pedido", "Cancelar"
        );

        if (janela.abrir() != 0) {
            return null;
        }

        return new LocalDate[] {
                paraLocalDate(campoRetirada.getDate()),
                paraLocalDate(campoRetorno.getDate())
        };
    }

    private static JDateChooser criarCampoData(LocalDate valorInicial) {

        JDateChooser campo = new JDateChooser(paraDate(valorInicial), "dd/MM/yyyy");
        campo.setLocale(new Locale("pt", "BR"));
        campo.setFont(new Font("Tahoma", Font.PLAIN, 16));
        campo.setPreferredSize(new Dimension(180, 36));

        return campo;
    }

    private static JLabel criarRotulo(String texto) {

        JLabel rotulo = new JLabel(texto);
        rotulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        rotulo.setForeground(VERDE_ESCURO);

        return rotulo;
    }

    private static Date paraDate(LocalDate data) {

        return data == null ? null : Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static LocalDate paraLocalDate(Date data) {

        return data == null ? null : data.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
