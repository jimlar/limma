package limma.application.video;

import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JLabel;

import limma.Command;
import limma.PlayerManager;
import limma.ui.Player;
import limma.ui.browser.SimpleNavigationNode;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;

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
