package limma.swing;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class TaskDialog extends Dialog {

    public TaskDialog(DialogManager dialogManager) {
        super(dialogManager);
    }

    public void executeInDialog(final Task task) {
        final JComponent jComponent = task.createComponent();
        try {
            new Thread() {
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            add(jComponent);
                            open();
                        }
                    });
                    try {
                        task.run();
                    } finally {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                close();
                            }
                        });
                    }
                }
            }.start();
        } finally {
            remove(jComponent);
        }
    }
}
