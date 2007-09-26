package limma.ui.dialogs;

import javax.swing.JComponent;

public interface DialogManager {

    JComponent getDialogManagerComponent();

    void setRoot(JComponent component);

    void executeInDialog(Task task);

    void open(LimmaDialog dialog);

    void close(LimmaDialog dialog);

    LimmaDialog getTopDialog();

    LimmaDialog createAndOpen(Class dialogClass);
}