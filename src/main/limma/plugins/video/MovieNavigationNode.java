package limma.plugins.video;

import limma.swing.navigationlist.DefaultNavigationNode;
import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;

import javax.swing.*;
import java.io.IOException;

public class MovieNavigationNode extends DefaultNavigationNode {
    private Video video;
    private VideoPlayer player;
    private DialogManager dialogManager;

    public MovieNavigationNode(Video video, VideoPlayer player, DialogManager dialogManager) {
        super(video.getTitle());
        this.video = video;
        this.player = player;
        this.dialogManager = dialogManager;
    }

    public Video getVideo() {
        return video;
    }

    public void performAction() {
        dialogManager.executeInDialog(new Task() {
            public JComponent createComponent() {
                return new AntialiasLabel("Playing " + video.getTitle() + "...");
            }

            public void run() {
                try {
                    player.play(getVideo());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
