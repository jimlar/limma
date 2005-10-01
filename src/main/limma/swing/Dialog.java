package limma.swing;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Dialog extends JPanel {
    private static final int PADDING = 10;
    private DialogManager dialogManager;

    public Dialog(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
        Border emptyBorder = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.white, 1), emptyBorder));
        setBackground(Color.black);
        setVisible(false);
    }

    public void invalidate() {
        setSize(getPreferredSize());
        setBounds(getParent().getWidth() / 2 - getWidth() / 2, getParent().getHeight() / 2 - getHeight() / 2, getWidth(), getHeight());
        super.invalidate();
    }

    protected void open() {
        dialogManager.open(this);
    }

    protected void close() {
        dialogManager.close(this);
    }
}
