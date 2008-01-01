package limma.ui.video;

import limma.application.Command;
import limma.application.video.IMDBService;
import limma.application.video.VideoConfig;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.AntialiasLabel;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.LimmaDialog;

import javax.swing.*;

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


    public boolean consume(Command command) {
        switch (command) {
            case EXIT:
                close();
                return true;
            case ACTION:
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
