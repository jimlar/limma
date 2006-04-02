package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.navigation.NavigationNode;

import java.util.*;

public class NewMoviesNode extends NavigationNode {
    private MovieStorage movieStorage;
    private int numberOfDays;
    private VideoPlayer videoPlayer;
    private PersistenceManager persistenceManager;

    public NewMoviesNode(MovieStorage movieStorage, int numberOfDays, VideoPlayer videoPlayer, PersistenceManager persistenceManager) {
        this.movieStorage = movieStorage;
        this.numberOfDays = numberOfDays;
        this.videoPlayer = videoPlayer;
        this.persistenceManager = persistenceManager;
    }

    public String getTitle() {
        return "New (" + numberOfDays + " days)";
    }

    public List<NavigationNode> getChildren() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_YEAR, -numberOfDays);
        Date boundaryDate = calendar.getTime();

        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        for (Iterator i = movieStorage.getVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            if (video.getCreated() != null && boundaryDate.before(video.getCreated())) {

                MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, movieStorage, persistenceManager);
                movieNode.setParent(this);
                children.add(movieNode);
            }
        }

        Collections.sort(children, new Comparator<NavigationNode>() {
            public int compare(NavigationNode o1, NavigationNode o2) {
                MovieNavigationNode node1 = (MovieNavigationNode) o1;
                MovieNavigationNode node2 = (MovieNavigationNode) o2;
                return node2.getVideo().getCreated().compareTo(node1.getVideo().getCreated());
            }
        });

        return children;
    }
}
