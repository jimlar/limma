package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.navigation.NavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllMoviesNode extends NavigationNode {
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private PersistenceManager persistenceManager;

    public AllMoviesNode(MovieStorage movieStorage, VideoPlayer videoPlayer, PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
    }

    public String getTitle() {
        return "All";
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        for (Iterator i = movieStorage.getVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, movieStorage, persistenceManager);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
