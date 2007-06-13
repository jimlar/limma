package limma.plugins.video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.swing.navigation.NavigationNode;

public class AllMoviesNode extends NavigationNode {
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public AllMoviesNode(MovieStorage movieStorage, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return "All";
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        for (Iterator i = movieStorage.getVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, movieStorage, videoRepository);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
