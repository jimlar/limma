package limma.plugins.video;

import limma.swing.navigationlist.NavigationNode;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.PlayerManager;
import limma.Player;

import javax.swing.*;
import java.io.IOException;

class PlayDVDDiscNode extends NavigationNode {
    private final DialogManager dialogManager;
    private final VideoConfig videoConfig;
    private PlayerManager playerManager;

    public PlayDVDDiscNode(DialogManager dialogManager, VideoConfig videoConfig, PlayerManager playerManager) {
        super("Play DVD Disc");
        this.dialogManager = dialogManager;
        this.videoConfig = videoConfig;
        this.playerManager = playerManager;
    }

    public void performAction() {
        dialogManager.executeInDialog(new Task() {
            public JComponent createComponent() {
                return new AntialiasLabel("Playing DVD Disc...");
            }

            public void run() {
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
