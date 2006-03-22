package limma.plugins.video;

import java.util.ArrayList;
import java.util.List;

import limma.swing.menu.NavigationNode;

public class MovieNavigationNode extends NavigationNode {
    private Video video;
    private VideoPlayer player;
    private MovieNodeFactory movieNodeFactory;

    public MovieNavigationNode(Video video, VideoPlayer player, MovieNodeFactory movieNodeFactory) {
        this.movieNodeFactory = movieNodeFactory;
        this.video = video;
        this.player = player;
    }

    public Video getVideo() {
        return video;
    }

    public String getTitle() {
        return video.getTitle();
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        NavigationNode updateFromIMDBNode = movieNodeFactory.createUpdateFromIMDBNode(video);
        updateFromIMDBNode.setParent(this);
        children.add(updateFromIMDBNode);

        NavigationNode editTagsNode = movieNodeFactory.createEditTagsNode(video);
        editTagsNode.setParent(this);
        children.add(editTagsNode);
        return children;
    }

    public void performAction() {
        player.play(getVideo());
    }
}
