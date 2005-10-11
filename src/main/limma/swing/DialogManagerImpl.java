package limma.swing;

import javax.swing.*;
import java.util.Stack;

public class DialogManagerImpl implements DialogManager {
    private Stack dialogStack = new Stack();
    private JLayeredPane layeredPane;

    public DialogManagerImpl() {
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
    }

    public JComponent getDialogManagerComponent() {
        return layeredPane;
    }

    public void setRoot(JComponent component) {
        layeredPane.add(component, new Integer(0));
    }

    public void executeInDialog(Task task) {
        TaskDialog dialog = new TaskDialog(this);
        dialog.executeInDialog(task);
    }

    public synchronized void open(LimmaDialog dialog) {
        dialogStack.push(dialog);
        layeredPane.add(dialog, new Integer(JLayeredPane.POPUP_LAYER.intValue() + dialogStack.size()));
        dialog.setVisible(true);
        dialog.invalidate();
        dialog.validate();
    }

    public synchronized void close(LimmaDialog dialog) {
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
}
