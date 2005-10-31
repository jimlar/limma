package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;
import limma.swing.TransactionalTask;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class IMDBDialog extends LimmaDialog {
    private DialogManager dialogManager;
    private JTextField textField;
    private Video video;
    private IMDBSevice imdbSevice;
    private PersistenceManager persistenceManager;

    public IMDBDialog(DialogManager dialogManager, Video video, IMDBSevice imdbSevice, PersistenceManager persistenceManager) {
        super(dialogManager);
        this.dialogManager = dialogManager;
        this.video = video;
        this.imdbSevice = imdbSevice;
        this.persistenceManager = persistenceManager;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new AntialiasLabel("IMDB Number:"));
        textField = new JTextField();
        textField.setOpaque(true);
        textField.setColumns(10);
        textField.setFont(AntialiasLabel.DEFAULT_FONT);
        panel.add(textField);
        add(panel);
    }

    public void open() {
        super.open();
        textField.setText(video.getImdbNumber() != 0 ? String.valueOf(video.getImdbNumber()) : "");
        textField.requestFocusInWindow();
    }

    public boolean keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                close();
                return true;
            case KeyEvent.VK_ENTER:
                updateFromImdb();
                close();
                return true;
        }
        return false;
    }

    private void updateFromImdb() {
        dialogManager.executeInDialog(new TransactionalTask(persistenceManager) {
            public JComponent createComponent() {
                return new AntialiasLabel("Fetching information from IMDB...");
            }

            public void runInTransaction(Session session) {
                try {
                    final IMDBInfo info = imdbSevice.getInfo(getImdbNumber());
                    video.setImdbNumber(info.getImdbNumber());
                    video.setTitle(info.getTitle());
                    video.setDirector(info.getDirector());
                    video.setRuntime(info.getRuntime());
                    video.setPlot(info.getPlot());
                    video.setRating(info.getRating());
                    video.setYear(info.getYear());
                    session.merge(video);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private int getImdbNumber() {
        return Integer.parseInt(textField.getText());
    }
}
