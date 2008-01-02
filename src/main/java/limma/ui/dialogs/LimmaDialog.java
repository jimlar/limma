package limma.ui.dialogs;

import limma.application.CommandConsumer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public abstract class LimmaDialog extends JPanel implements CommandConsumer {
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
        if (getParent() != null) {
            setBounds(getParent().getWidth() / 2 - getWidth() / 2, getParent().getHeight() / 2 - getHeight() / 2, getWidth(), getHeight());
        }
        super.invalidate();
    }

    public void open() {
        dialogManager.open(this);
    }

    public void close() {
        dialogManager.close(this);
    }

    public DialogManager getDialogManager() {
        return dialogManager;
    }
}
