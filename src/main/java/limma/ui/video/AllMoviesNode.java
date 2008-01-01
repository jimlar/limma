package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllMoviesNode extends BrowserModelNode {
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;
    private List<Video> allVideos = new ArrayList<Video>();
    private Date lastUpdated;

    public AllMoviesNode(VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;

        refreshVideosIfNeeded();
    }

    private void refreshVideosIfNeeded() {
        if (lastUpdated == null || lastUpdated.before(videoRepository.getLastUpdated())) {
            allVideos = videoRepository.getAllVideos();
            lastUpdated = videoRepository.getLastUpdated();
        }
    }

    public String getTitle() {
        return "All";
    }

    public List<BrowserModelNode> getChildren() {
        refreshVideosIfNeeded();

        ArrayList<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
        for (Object o : allVideos) {
            Video video = (Video) o;
            MovieBrowserNode movieNode = new MovieBrowserNode(video, videoPlayer, videoRepository);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
