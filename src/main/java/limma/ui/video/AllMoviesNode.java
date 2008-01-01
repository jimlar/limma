package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AllMoviesNode extends BrowserModelNode {
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public AllMoviesNode(VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return "All";
    }

    public List<BrowserModelNode> getChildren() {
        ArrayList<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
        for (Iterator i = videoRepository.getAllVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            MovieBrowserNode movieNode = new MovieBrowserNode(video, videoPlayer, videoRepository);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
