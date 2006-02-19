package limma.plugins.video;

import limma.swing.menu.MenuNode;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class AllMoviesNode extends MenuNode {
    private MovieStorage movieStorage;
    private MovieNodeFactory movieNodeFactory;

    public AllMoviesNode(MovieStorage movieStorage, MovieNodeFactory movieNodeFactory) {
        this.movieStorage = movieStorage;
        this.movieNodeFactory = movieNodeFactory;
    }

    public String getTitle() {
        return "All";
    }

    public void performAction() {
    }

    public List<MenuNode> getChildren() {
        ArrayList<MenuNode> children = new ArrayList<MenuNode>();
        for (Iterator i = movieStorage.getVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            MovieMenuNode movieNode = movieNodeFactory.createMovieNode(video);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
