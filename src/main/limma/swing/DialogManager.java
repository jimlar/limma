package limma.swing;

import javax.swing.*;

public interface DialogManager {

    JComponent getDialogManagerComponent();

    void setRoot(JComponent component);

    void executeInDialog(Task task);

    void open(LimmaDialog dialog);

    void close(LimmaDialog dialog);

    LimmaDialog getTopDialog();
}
