package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;
import limma.UIProperties;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class IMDBDialog extends LimmaDialog {
    private DialogManager dialogManager;
    private JTextField textField;
    private Video video;
    private IMDBSevice imdbSevice;
    private PersistenceManager persistenceManager;
    private VideoConfig videoConfig;
    private UIProperties uiProperties;

    public IMDBDialog(DialogManager dialogManager, Video video, IMDBSevice imdbSevice, PersistenceManager persistenceManager, VideoConfig videoConfig, UIProperties uiProperties) {
        super(dialogManager);
        this.dialogManager = dialogManager;
        this.video = video;
        this.imdbSevice = imdbSevice;
        this.persistenceManager = persistenceManager;
        this.videoConfig = videoConfig;
        this.uiProperties = uiProperties;

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new AntialiasLabel("IMDB Number:", uiProperties));
        textField = new JTextField();
        textField.setOpaque(true);
        textField.setColumns(10);
        textField.setFont(uiProperties.getSmallFont());
        panel.add(textField);
        add(panel);
    }

    public void open() {
        super.open();
        textField.setText("");
        if (video.hasImdbNumber()) {
            textField.setText(String.valueOf(video.getImdbNumber()));
        } else {
            dialogManager.executeInDialog(new DetectImdbNumberTask(video, textField, uiProperties));
        }
        textField.requestFocusInWindow();
    }

    public boolean keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                close();
                return true;
            case KeyEvent.VK_ENTER:
                dialogManager.executeInDialog(new UpdateFromImdbTask(persistenceManager, imdbSevice, videoConfig, video, getEnteredImdbNumber(), uiProperties));
                close();
                return true;
        }
        return false;
    }

    private int getEnteredImdbNumber() {
        return Integer.parseInt(textField.getText());
    }

}
