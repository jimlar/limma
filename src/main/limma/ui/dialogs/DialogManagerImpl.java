package limma.ui.dialogs;

import java.util.Stack;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import limma.ui.UIProperties;


public class DialogManagerImpl implements DialogManager {
    private Stack<LimmaDialog> dialogStack = new Stack<LimmaDialog>();
    private JLayeredPane layeredPane;
    private TaskDialog dialog;
    private DialogFactory dialogFactory;

    public DialogManagerImpl(DialogFactory dialogFactory, UIProperties uiProperties) {
        this.dialogFactory = dialogFactory;
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
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

    public synchronized void open(LimmaDialog dialog) {
        if (dialogStack.contains(dialog)) {
            return;
        }
        dialogStack.push(dialog);
        layeredPane.add(dialog, new Integer(JLayeredPane.POPUP_LAYER.intValue() + dialogStack.size()));
        dialog.setVisible(true);
        dialog.invalidate();
        dialog.validate();
    }

    public synchronized void close(LimmaDialog dialog) {
        if (!dialogStack.contains(dialog)) {
            return;
        }
        dialogStack.remove(dialog);
        dialog.setVisible(false);
        layeredPane.remove(dialog);
    }

    public synchronized LimmaDialog getTopDialog() {
        if (dialogStack.isEmpty()) {
            return null;
        }
        return (LimmaDialog) dialogStack.peek();
    }

    public LimmaDialog createAndOpen(Class dialogClass) {
        LimmaDialog limmaDialog = dialogFactory.createDialog(dialogClass);
        limmaDialog.open();
        return limmaDialog;
    }
}
