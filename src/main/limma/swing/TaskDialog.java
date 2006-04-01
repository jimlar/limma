package limma.swing;

import limma.UIProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class TaskDialog extends LimmaDialog {
    private UIProperties uiProperties;

    public TaskDialog(DialogManager dialogManager, UIProperties uiProperties) {
        super(dialogManager);
        this.uiProperties = uiProperties;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void executeInDialog(final Task task) {

        final JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        final AntialiasLabel status = new AntialiasLabel("", uiProperties);
        panel.add(status, BorderLayout.CENTER);

        final TaskFeedback feedback = new TaskFeedback() {
            public void setStatusMessage(final String message) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        status.setText(message);
                    }
                });
            }
        };

        new Thread() {
            public void run() {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        add(panel);
                        if (getComponentCount() == 1) {
                            open();
                        }
                        invalidate();
                        repaint();
                    }
                });
                try {
                    task.run(feedback);
                } finally {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            remove(panel);
                            if (getComponentCount() == 0) {
                                close();
                            } else {
                                doLayout();
                                invalidate();
                                repaint();
                            }
                        }
                    });
                }
            }
        }.start();
    }


    public boolean keyPressed(KeyEvent e) {
        return true;
    }
}
