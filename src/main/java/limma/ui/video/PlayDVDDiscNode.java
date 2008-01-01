package limma.ui.video;

import limma.application.Command;
import limma.application.PlayerManager;
import limma.application.video.VideoConfig;
import limma.ui.Player;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;

import javax.swing.*;
import java.io.IOException;

class PlayDVDDiscNode extends SimpleBrowserNode {
    private final DialogManager dialogManager;
    private final VideoConfig videoConfig;
    private PlayerManager playerManager;

    public PlayDVDDiscNode(DialogManager dialogManager, VideoConfig videoConfig, PlayerManager playerManager) {
        super("Play DVD Disc");
        this.dialogManager = dialogManager;
        this.videoConfig = videoConfig;
        this.playerManager = playerManager;
    }

    public void performAction(DialogManager dialogManager) {
        this.dialogManager.executeInDialog(new Task() {

            public void run(TaskFeedback feedback) {
                feedback.setStatusMessage("Playing DVD Disc...");
                playerManager.switchTo(new Player() {
                    public JComponent getPlayerPane() {
                        return new JLabel("Playing DVD Disc");
                    }

                    public boolean consume(Command command) {
                        return false;
                    }
                });
                try {
                    videoConfig.getPlayDvdDiscCommand().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
