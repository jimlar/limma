package limma.swing;

import javax.swing.*;
import java.awt.*;

public class AntialiasLabel extends JLabel {

    public AntialiasLabel() {
        this("");
    }

    public AntialiasLabel(String text) {
        setText(text);
        Font font = Font.decode("SansSerif");
        font = font.deriveFont(Font.BOLD);
        font = font.deriveFont((float) 30);
        setFont(font);
        setOpaque(false);
        setForeground(Color.white);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
    }
}
