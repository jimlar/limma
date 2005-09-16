package limma;

import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.ImageIcon;

public class ImagePanel extends JPanel {
    private final ImageIcon background;

    public ImagePanel(ImageIcon image) {
        this.background = image;
    }

    public void paint(Graphics g) {
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
        super.paint(g);
    }
}
