package limma.plugins.video;

import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.swing.*;
import org.hibernate.Session;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class EditMovieDialog extends LimmaDialog {
    private DialogManager dialogManager;
    private JTextField textField;
    private Video video;
    private PersistenceManager persistenceManager;

    public EditMovieDialog(DialogManager dialogManager, PersistenceManager persistenceManager, UIProperties uiProperties) {
        super(dialogManager);
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new AntialiasLabel("Title:", uiProperties));
        textField = new JTextField();
        textField.setOpaque(true);
        textField.setColumns(30);
        textField.setFont(uiProperties.getSmallFont());
        panel.add(textField);
        add(panel);
    }

    public void open() {
        super.open();
        textField.requestFocusInWindow();
    }

    public void setVideo(Video video) {
        this.video = video;
        textField.setText(video.getTitle());
    }

    public boolean keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                close();
                return true;
            case KeyEvent.VK_ENTER:
                dialogManager.executeInDialog(new SaveTitleTask(persistenceManager, video, textField.getText()));
                close();
                return true;
        }
        return false;
    }

    private static class SaveTitleTask extends TransactionalTask {
        private Video video;
        private String newTitle;

        public SaveTitleTask(PersistenceManager persistenceManager, Video video, String newTitle) {
            super(persistenceManager);
            this.video = video;
            this.newTitle = newTitle;
        }

        public void runInTransaction(TaskFeedback feedback, Session session) {
            feedback.setStatusMessage("Saving...");
            video.setTitle(newTitle);
            session.merge(video);
        }
    }
}
