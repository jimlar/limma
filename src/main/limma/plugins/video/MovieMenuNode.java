package limma.plugins.video;

import limma.swing.menu.MenuNode;
import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.TaskInfo;
import limma.UIProperties;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class MovieMenuNode extends MenuNode {
    private Video video;
    private VideoPlayer player;
    private DialogManager dialogManager;
    private UIProperties uiProperties;
    private MovieNodeFactory movieNodeFactory;

    public MovieMenuNode(Video video, VideoPlayer player, DialogManager dialogManager, UIProperties uiProperties, MovieNodeFactory movieNodeFactory) {
        this.movieNodeFactory = movieNodeFactory;
        this.video = video;
        this.player = player;
        this.dialogManager = dialogManager;
        this.uiProperties = uiProperties;
    }

    public Video getVideo() {
        return video;
    }

    public String getTitle() {
        return video.getTitle();
    }

    public List<MenuNode> getChildren() {
        ArrayList<MenuNode> children = new ArrayList<MenuNode>();
        MenuNode updateFromIMDBNode = movieNodeFactory.createUpdateFromIMDBNode(video);
        updateFromIMDBNode.setParent(this);
        children.add(updateFromIMDBNode);

        MenuNode editTagsNode = movieNodeFactory.createEditTagsNode(video);
        editTagsNode.setParent(this);
        children.add(editTagsNode);
        return children;
    }

    public void performAction() {
        dialogManager.executeInDialog(new Task() {
            public JComponent prepareToRun(TaskInfo taskInfo) {
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
