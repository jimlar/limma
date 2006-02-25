package limma.swing;

import javax.swing.*;

public interface Task extends Runnable {
    JComponent prepareToRun(TaskInfo taskInfo);
}
