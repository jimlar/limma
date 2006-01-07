package limma.swing;

import javax.swing.*;
import java.awt.*;

public class AntialiasLabel extends JLabel {
    public static final Font DEFAULT_FONT = Font.decode("Verdana").deriveFont(Font.BOLD).deriveFont((float) 20);

    public AntialiasLabel() {
        this("");
    }

    public AntialiasLabel(String text) {
        setText(text);
        setFont(DEFAULT_FONT);
        setOpaque(false);
        setForeground(Color.white);
    }

    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
    }
}
