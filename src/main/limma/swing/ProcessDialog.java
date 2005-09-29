package limma.swing;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ProcessDialog extends JPanel {
    private JDesktopPane desktopPane;
    private static final int PADDING = 10;

    public ProcessDialog(JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
        Border emptyBorder = BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Please wait...");
        titledBorder.setTitleColor(Color.white);
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        setBorder(BorderFactory.createCompoundBorder(titledBorder, emptyBorder));
        setBackground(Color.black);
    }

    public void executeInDialog(final Job job) {
        final JComponent jComponent = job.init(this);
        try {
            new Thread() {
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            desktopPane.add(ProcessDialog.this, JDesktopPane.MODAL_LAYER);
                            add(jComponent);
                            setVisible(true);
                            pack();
                        }
                    });
                    try {
                        job.run();
                    } finally {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                setVisible(false);
                                desktopPane.remove(ProcessDialog.this);
                            }
                        });
                    }
                }
            }.start();
        } finally {
            remove(jComponent);
        }
    }

    public void pack() {
        setSize(getPreferredSize());
        validate();
        setBounds(getParent().getWidth() / 2 - getWidth() / 2,
                  getParent().getHeight() / 2 - getHeight() / 2,
                  getWidth(),
                  getHeight());
    }

    public static interface Job extends Runnable {
        JComponent init(ProcessDialog dialog);
    }
}
