package limma.swing;

import javax.swing.*;

public interface TaskInfo {

    void setMessage(String message);

    BoundedRangeModel getProgressModel();
}
