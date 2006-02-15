package limma.plugins.video;

import limma.swing.navigationlist.MenuNode;
import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.UIProperties;

import javax.swing.*;
import java.io.IOException;

public class MovieMenuNode extends MenuNode {
    private Video video;
    private VideoPlayer player;
    private DialogManager dialogManager;
    private UIProperties uiProperties;

    public MovieMenuNode(Video video, VideoPlayer player, DialogManager dialogManager, UIProperties uiProperties) {
        super(video.getTitle());
        this.video = video;
        this.player = player;
        this.dialogManager = dialogManager;
        this.uiProperties = uiProperties;
    }

    public Video getVideo() {
        return video;
    }

    public void performAction() {
        dialogManager.executeInDialog(new Task() {
            public JComponent createComponent() {
                return new AntialiasLabel("Playing " + video.getTitle() + "...", uiProperties);
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
