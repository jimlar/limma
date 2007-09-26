package limma.ui;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
    private final ImageIcon background;

    public ImagePanel(ImageIcon image) {
        this.background = image;
        setOpaque(false);
    }

    protected void paintComponent(Graphics g) {
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        super.paintComponent(g);
    }
}
