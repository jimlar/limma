package limma.swing;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class LimmaDialog extends JPanel {
    private static final int PADDING = 10;
    private DialogManager dialogManager;

    public LimmaDialog(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
        Border emptyBorder = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1), emptyBorder));
        setBackground(Color.black);
        setVisible(false);
        setOpaque(false);
    }

    public void invalidate() {
        setSize(getPreferredSize());
        setBounds(getParent().getWidth() / 2 - getWidth() / 2, getParent().getHeight() / 2 - getHeight() / 2, getWidth(), getHeight());
        super.invalidate();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        Composite oldComposite = g2d.getComposite();

        Composite alphaComp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f);
        g2d.setComposite(alphaComp);
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setComposite(oldComposite);
    }

    public void open() {
        dialogManager.open(this);
    }

    public void close() {
        dialogManager.close(this);
    }

    public abstract void keyPressed(KeyEvent e);
}
