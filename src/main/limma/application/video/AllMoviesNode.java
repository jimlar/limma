package limma.application.video;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.NavigationNode;

public class AllMoviesNode extends NavigationNode {
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public AllMoviesNode(VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return "All";
    }

    public List<NavigationNode> getChildren() {
        ArrayList<NavigationNode> children = new ArrayList<NavigationNode>();
        for (Iterator i = videoRepository.getAllVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, videoRepository);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
