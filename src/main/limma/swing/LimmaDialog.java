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
        setBackground(new Color(0, 0, 0, 0.8f));
        setVisible(false);
        setOpaque(true);
    }

    public void invalidate() {
        setSize(getPreferredSize());
        setBounds(getParent().getWidth() / 2 - getWidth() / 2, getParent().getHeight() / 2 - getHeight() / 2, getWidth(), getHeight());
        super.invalidate();
    }

    public void open() {
        dialogManager.open(this);
    }

    public void close() {
        dialogManager.close(this);
    }

    public abstract boolean keyPressed(KeyEvent e);
}
