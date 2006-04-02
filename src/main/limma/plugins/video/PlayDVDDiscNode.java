package limma.plugins.video;

import limma.Player;
import limma.PlayerManager;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.TaskFeedback;
import limma.swing.navigation.SimpleNavigationNode;

import javax.swing.*;
import java.io.IOException;

class PlayDVDDiscNode extends SimpleNavigationNode {
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

                    public void next() {
                    }

                    public void previous() {
                    }

                    public void ff() {
                    }

                    public void rew() {
                    }

                    public void pause() {
                    }

                    public void stop() {
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
