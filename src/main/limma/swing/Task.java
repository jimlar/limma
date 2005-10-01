package limma.swing;

import javax.swing.*;

public interface Task extends Runnable {
    JComponent createComponent();
}
