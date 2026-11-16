package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

// Barra de rolagem do sistema: fina, sem setas, com a alça verde arredondada.
// Vale para todas as telas depois de chamar BarraRolagem.instalar() no Main.
public class BarraRolagem extends BasicScrollBarUI {

    private static final int LARGURA = 16;

    // Verde-claro com contorno verde-escuro: aparece tanto no fundo claro
    // quanto em cima dos painéis verde-escuros
    private static final Color ALCA = new Color(114, 219, 145);
    private static final Color ALCA_HOVER = new Color(90, 200, 125);
    private static final Color CONTORNO = Cores.VERDE;

    // Faz todas as barras de rolagem criadas depois daqui usarem este estilo
    public static void instalar() {
        UIManager.put("ScrollBarUI", BarraRolagem.class.getName());
        UIManager.put("ScrollBar.width", LARGURA);

        // Fundo atrás da barra (era cinza) na cor de fundo do sistema
        UIManager.put("ScrollPane.background", Cores.FUNDO);
    }

    // O Swing chama este método para criar a barra de cada JScrollPane
    public static ComponentUI createUI(JComponent c) {
        return new BarraRolagem();
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        scrollbar.setOpaque(false);
        scrollBarWidth = LARGURA;
    }

    @Override
    protected JButton createDecreaseButton(int orientacao) {
        return botaoInvisivel();
    }

    @Override
    protected JButton createIncreaseButton(int orientacao) {
        return botaoInvisivel();
    }

    private JButton botaoInvisivel() {
        JButton botao = new JButton();
        Dimension zero = new Dimension(0, 0);
        botao.setPreferredSize(zero);
        botao.setMinimumSize(zero);
        botao.setMaximumSize(zero);
        return botao;
    }

    @Override
    protected Dimension getMinimumThumbSize() {
        return new Dimension(LARGURA, 40);
    }

    @Override
    protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        // Sem trilho: só a alça aparece
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle r) {

        if (r.isEmpty() || !scrollbar.isEnabled()) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = r.x + 2;
        int y = r.y + 2;
        int largura = r.width - 4;
        int altura = r.height - 4;
        int arco = Math.min(largura, altura);

        g2.setColor(isThumbRollover() || isDragging ? ALCA_HOVER : ALCA);
        g2.fillRoundRect(x, y, largura, altura, arco, arco);

        g2.setColor(CONTORNO);
        g2.drawRoundRect(x, y, largura - 1, altura - 1, arco, arco);

        g2.dispose();
    }
}
