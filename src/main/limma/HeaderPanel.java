package limma;

import limma.swing.AntialiasLabel;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {

    public HeaderPanel() {
        AntialiasLabel title = new AntialiasLabel("Limma");
        title.setFont(Font.decode("Verdana").deriveFont((float) 40));
        title.setForeground(Color.black);
        add(title);
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        paintGradient(graphics);

        drawLine(graphics, 0, new Color(0xbabfc3));
        drawLine(graphics, getHeight() - 2, new Color(0x79949d));
        drawLine(graphics, getHeight() - 1, new Color(0x99aab1));

        super.paintComponent(g);
    }

    private void drawLine(Graphics2D graphics, int y, Color color) {
        graphics.setColor(color);
        graphics.drawLine(0, y, getWidth(), y);
    }

    private void paintGradient(Graphics2D graphics) {
        Color topColor = new Color(0xf0f7fd);
        Color bottomColor = new Color(0xa4c0cc);
        int height = getHeight();
        int yPos = 0;
        graphics.setPaint(new GradientPaint(0, yPos, topColor, 0, yPos + height, bottomColor));
        graphics.fillRect(0, yPos, getWidth(), height);
    }
}
