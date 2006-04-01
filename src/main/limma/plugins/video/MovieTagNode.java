package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.menu.NavigationNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieTagNode extends NavigationNode {
    private String tag;
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private PersistenceManager persistenceManager;

    public MovieTagNode(String tag, MovieStorage movieStorage, VideoPlayer videoPlayer, PersistenceManager persistenceManager) {
        this.tag = tag;
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
        this.persistenceManager = persistenceManager;
    }

    public String getTitle() {
        return tag;
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        List<Video> videosWithTag = movieStorage.getVideosWithTag(tag);
        for (Iterator<Video> i = videosWithTag.iterator(); i.hasNext();) {
            Video video = i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, movieStorage, persistenceManager);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
