package limma.plugins.video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.swing.navigation.NavigationNode;

public class MovieTagNode extends NavigationNode {
    private String tag;
    private MovieStorage movieStorage;
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public MovieTagNode(String tag, MovieStorage movieStorage, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.tag = tag;
        this.movieStorage = movieStorage;
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return tag;
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        List<Video> videosWithTag = movieStorage.getVideosWithTag(tag);
        for (Iterator<Video> i = videosWithTag.iterator(); i.hasNext();) {
            Video video = i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, movieStorage, videoRepository);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
