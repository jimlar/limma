package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MovieTagNode extends BrowserModelNode {
    private String tag;
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public MovieTagNode(String tag, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        this.tag = tag;
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return tag;
    }

    public List<BrowserModelNode> getChildren() {
        ArrayList<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
        List<Video> videosWithTag = videoRepository.getVideosWithTag(tag);
        for (Iterator<Video> i = videosWithTag.iterator(); i.hasNext();) {
            Video video = i.next();
            MovieBrowserNode movieNode = new MovieBrowserNode(video, videoPlayer, videoRepository);
            movieNode.setParent(this);
            children.add(movieNode);
        }
        return children;
    }
}
