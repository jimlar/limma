package limma.application.video;

import javax.swing.JPanel;
import javax.swing.JTextField;

import limma.Command;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.AntialiasLabel;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.LimmaDialog;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;

public class EditMovieDialog extends LimmaDialog {
    private DialogManager dialogManager;
    private JTextField textField;
    private Video video;
    private VideoRepository videoRepository;

    public EditMovieDialog(DialogManager dialogManager, UIProperties uiProperties, VideoRepository videoRepository) {
        super(dialogManager);
        this.dialogManager = dialogManager;
        this.videoRepository = videoRepository;

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


    public boolean consume(Command command) {
        switch (command) {
            case EXIT:
                close();
                return true;
            case ACTION:
                dialogManager.executeInDialog(new SaveTitleTask(video, textField.getText(), videoRepository));
                close();
                return true;
        }
        return false;
    }

    private static class SaveTitleTask implements Task {
        private Video video;
        private String newTitle;
        private VideoRepository videoRepository;

        public SaveTitleTask(Video video, String newTitle, VideoRepository videoRepository) {
            this.video = video;
            this.newTitle = newTitle;
            this.videoRepository = videoRepository;
        }

        public void run(TaskFeedback feedback) {
            feedback.setStatusMessage("Saving...");
            video.setTitle(newTitle);
            videoRepository.save(video);
        }
    }
}
