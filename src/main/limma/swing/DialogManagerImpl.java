package limma.swing;

import javax.swing.*;

public class DialogManagerImpl implements DialogManager {
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

    public void open(Dialog dialog) {
        layeredPane.add(dialog, JLayeredPane.POPUP_LAYER);
        dialog.setVisible(true);
        dialog.invalidate();
        dialog.validate();
    }

    public void close(Dialog dialog) {
        dialog.setVisible(false);
        layeredPane.remove(dialog);
    }
}
