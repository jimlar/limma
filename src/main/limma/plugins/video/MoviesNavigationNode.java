package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.menu.NavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoviesNavigationNode extends NavigationNode {
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private PersistenceManager persistenceManager;
    private List<NavigationNode> trailingNodes = new ArrayList<NavigationNode>();

    public MoviesNavigationNode(MovieStorage movieStorage, VideoPlayer videoPlayer, PersistenceManager persistenceManager) {
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
        this.persistenceManager = persistenceManager;
    }

    public String getTitle() {
        return "Movies";
    }

    public void addTrailingNode(NavigationNode navigationNode) {
        trailingNodes.add(navigationNode);
        navigationNode.setParent(this);
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

        children.addAll(trailingNodes);
        return children;
    }
}
