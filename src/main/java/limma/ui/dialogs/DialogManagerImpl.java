package limma.ui.dialogs;

import limma.ui.UIProperties;

import javax.swing.*;
import java.util.Stack;


public class DialogManagerImpl implements DialogManager {
    private Stack<LimmaDialog> dialogStack = new Stack<LimmaDialog>();
    private JLayeredPane layeredPane;
    private TaskDialog dialog;
    private DialogFactory dialogFactory;

    public DialogManagerImpl(DialogFactory dialogFactory, UIProperties uiProperties) {
        this.dialogFactory = dialogFactory;
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setBorder(BorderFactory.createEmptyBorder());
        dialog = new TaskDialog(this, uiProperties);
    }

    public JComponent getDialogManagerComponent() {
        return layeredPane;
    }

    public void setRoot(JComponent component) {
        layeredPane.add(component, new Integer(0));
    }

    public void executeInDialog(Task task) {
        dialog.executeInDialog(task);
    }

    public synchronized void open(final LimmaDialog dialog) {
        if (dialogStack.contains(dialog)) {
            return;
        }
        dialogStack.push(dialog);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                layeredPane.add(dialog, new Integer(JLayeredPane.POPUP_LAYER + dialogStack.size()));
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dialog.setVisible(true);
            }
        });
    }

    public synchronized void close(final LimmaDialog dialog) {
        if (!dialogStack.contains(dialog)) {
            return;
        }
        dialogStack.remove(dialog);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dialog.setVisible(false);
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                layeredPane.remove(dialog);
            }
        });
    }

    public synchronized LimmaDialog getTopDialog() {
        if (dialogStack.isEmpty()) {
            return null;
        }
        return dialogStack.peek();
    }

    public LimmaDialog createAndOpen(Class dialogClass) {
        LimmaDialog limmaDialog = create(dialogClass);
        limmaDialog.open();
        return limmaDialog;
    }

    public LimmaDialog create(Class dialogClass) {
        return dialogFactory.createDialog(dialogClass);
    }
}
