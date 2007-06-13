package limma.plugins.video;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JTextField;

import limma.UIProperties;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;

public class IMDBDialog extends LimmaDialog {
    private DialogManager dialogManager;
    private JTextField textField;
    private Video video;
    private IMDBService imdbService;
    private VideoConfig videoConfig;
    private VideoRepository videoRepository;

    public IMDBDialog(DialogManager dialogManager, IMDBService imdbService, VideoConfig videoConfig, UIProperties uiProperties, VideoRepository videoRepository) {
        super(dialogManager);
        this.dialogManager = dialogManager;
        this.imdbService = imdbService;
        this.videoConfig = videoConfig;
        this.videoRepository = videoRepository;

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
        textField.requestFocusInWindow();
    }

    public void setVideo(Video video) {
        this.video = video;
        textField.setText("");
        if (video.hasImdbNumber()) {
            textField.setText(String.valueOf(video.getImdbNumber()));
        } else {
            dialogManager.executeInDialog(new DetectImdbNumberTask(video, textField));
        }
    }

    public boolean keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                close();
                return true;
            case KeyEvent.VK_ENTER:
                dialogManager.executeInDialog(new UpdateFromImdbTask(imdbService, videoConfig, video, getEnteredImdbNumber(), videoRepository));
                close();
                return true;
        }
        return false;
    }

    private int getEnteredImdbNumber() {
        return Integer.parseInt(textField.getText());
    }

}
