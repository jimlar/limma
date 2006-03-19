package limma.plugins.video;

import limma.swing.menu.NavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieTagNode extends NavigationNode {
    private String tag;
    private MovieStorage movieStorage;
    private MovieNodeFactory movieNodeFactory;

    public MovieTagNode(String tag, MovieStorage movieStorage, MovieNodeFactory movieNodeFactory) {
        this.tag = tag;
        this.movieStorage = movieStorage;
        this.movieNodeFactory = movieNodeFactory;
    }

    public String getTitle() {
        return tag;
    }

    public void performAction() {
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        List<Video> videosWithTag = movieStorage.getVideosWithTag(tag);
        for (Iterator<Video> i = videosWithTag.iterator(); i.hasNext();) {
            Video video = i.next();
            MovieNavigationNode movieNode = movieNodeFactory.createMovieNode(video);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
