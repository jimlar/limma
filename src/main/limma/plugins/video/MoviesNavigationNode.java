package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.navigation.NavigationNode;
import limma.swing.navigation.SimpleNavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoviesNavigationNode extends SimpleNavigationNode {
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private PersistenceManager persistenceManager;

    public MoviesNavigationNode(MovieStorage movieStorage, VideoPlayer videoPlayer, PersistenceManager persistenceManager) {
        super("Movies");
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
        this.persistenceManager = persistenceManager;
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        NavigationNode allMoviesNode = new AllMoviesNode(movieStorage, videoPlayer, persistenceManager);
        allMoviesNode.setParent(this);
        children.add(allMoviesNode);

        NavigationNode lastWeeksMoviesNode = new NewMoviesNode(movieStorage, 7, videoPlayer, persistenceManager);
        lastWeeksMoviesNode.setParent(this);
        children.add(lastWeeksMoviesNode);

        NavigationNode lastMonthMoviesNode = new NewMoviesNode(movieStorage, 30, videoPlayer, persistenceManager);
        lastMonthMoviesNode.setParent(this);
        children.add(lastMonthMoviesNode);

        for (Iterator<String> i = movieStorage.getTags().iterator(); i.hasNext();) {
            String tag = i.next();
            MovieTagNode movieTagNode = new MovieTagNode(tag, movieStorage, videoPlayer, persistenceManager);
            movieTagNode.setParent(this);
            children.add(movieTagNode);
        }

        children.addAll(super.getChildren());
        return children;
    }
}
