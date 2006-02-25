package limma.swing;

import limma.UIProperties;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.*;

public class TaskDialog extends LimmaDialog {
    private UIProperties uiProperties;

    public TaskDialog(DialogManager dialogManager, UIProperties uiProperties) {
        super(dialogManager);
        this.uiProperties = uiProperties;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    public void executeInDialog(final Task task) {

        final DefaultBoundedRangeModel boundedRangeModel = new DefaultBoundedRangeModel();
        JProgressBar jProgressBar = new JProgressBar(boundedRangeModel);
        final AntialiasLabel label = new AntialiasLabel(uiProperties);
        TaskInfo taskInfo = new TaskInfo() {
            public void setMessage(String message) {
                label.setText(message);
            }

            public BoundedRangeModel getProgressModel() {
                return boundedRangeModel;
            }  
        };
        final JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.add(jProgressBar, BorderLayout.WEST);
        panel.add(task.prepareToRun(taskInfo), BorderLayout.CENTER);

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
                    task.run();
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
