package limma.plugins.video;

import limma.swing.navigationlist.MenuNode;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.PlayerManager;
import limma.Player;
import limma.UIProperties;

import javax.swing.*;
import java.io.IOException;

class PlayDVDDiscNode extends MenuNode {
    private final DialogManager dialogManager;
    private final VideoConfig videoConfig;
    private PlayerManager playerManager;
    private UIProperties uiProperties;

    public PlayDVDDiscNode(DialogManager dialogManager, VideoConfig videoConfig, PlayerManager playerManager, UIProperties uiProperties) {
        super("Play DVD Disc");
        this.dialogManager = dialogManager;
        this.videoConfig = videoConfig;
        this.playerManager = playerManager;
        this.uiProperties = uiProperties;
    }

    public void performAction() {
        dialogManager.executeInDialog(new Task() {
            public JComponent createComponent() {
                return new AntialiasLabel("Playing DVD Disc...", uiProperties);
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
