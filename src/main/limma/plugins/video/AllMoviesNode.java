package limma.plugins.video;

import limma.swing.menu.NavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllMoviesNode extends NavigationNode {
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

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        for (Iterator i = movieStorage.getVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            MovieNavigationNode movieNode = movieNodeFactory.createMovieNode(video);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
