package limma.swing;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

public class ProcessDialog extends JDialog {

    public ProcessDialog(Frame owner) {
        super(owner);
        setUndecorated(true);
        setLayout(new BorderLayout());
    }

    public void executeInDialog(final Job job) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Please wait"));
        add(panel, BorderLayout.CENTER);
        job.init(panel);
        try {
            new Thread() {
                public void run() {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            pack();
                            setVisible(true);
                            System.out.println("Murkla");
                        }
                    });
                    try {
                        job.run();
                    } finally {
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                setVisible(false);
                            }
                        });
                    }
                }
            }.start();
        } finally {
            remove(panel);
        }
    }

    public static interface Job extends Runnable {
        void init(JPanel panel);
    }
}
