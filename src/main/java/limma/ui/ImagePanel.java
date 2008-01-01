package limma.ui;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private final ImageIcon background;

    public ImagePanel(ImageIcon image) {
        this.background = image;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder());
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        super.paintComponent(g);
    }
}
