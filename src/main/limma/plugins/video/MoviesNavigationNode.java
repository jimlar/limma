package limma.plugins.video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import limma.domain.video.VideoRepository;
import limma.swing.navigation.NavigationNode;
import limma.swing.navigation.SimpleNavigationNode;

public class MoviesNavigationNode extends SimpleNavigationNode {
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public MoviesNavigationNode(MovieStorage movieStorage, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super("Movies");
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        NavigationNode allMoviesNode = new AllMoviesNode(movieStorage, videoPlayer, videoRepository);
        allMoviesNode.setParent(this);
        children.add(allMoviesNode);

        NavigationNode lastWeeksMoviesNode = new NewMoviesNode(movieStorage, 7, videoPlayer, videoRepository);
        lastWeeksMoviesNode.setParent(this);
        children.add(lastWeeksMoviesNode);

        NavigationNode lastMonthMoviesNode = new NewMoviesNode(movieStorage, 30, videoPlayer, videoRepository);
        lastMonthMoviesNode.setParent(this);
        children.add(lastMonthMoviesNode);

        for (Iterator<String> i = movieStorage.getTags().iterator(); i.hasNext();) {
            String tag = i.next();
            MovieTagNode movieTagNode = new MovieTagNode(tag, movieStorage, videoPlayer, videoRepository);
            movieTagNode.setParent(this);
            children.add(movieTagNode);
        }

        children.addAll(super.getChildren());
        return children;
    }
}
