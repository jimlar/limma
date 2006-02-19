package limma.plugins.video;

import limma.swing.menu.MenuNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class MovieTagNode extends MenuNode {
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

    public List<MenuNode> getChildren() {
        ArrayList<MenuNode> children = new ArrayList<MenuNode>();
        List<Video> videosWithTag = movieStorage.getVideosWithTag(tag);
        for (Iterator<Video> i = videosWithTag.iterator(); i.hasNext();) {
            Video video = i.next();
            MovieMenuNode movieNode = movieNodeFactory.createMovieNode(video);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
