package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MoviesBrowserNode extends SimpleBrowserNode {
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;

    public MoviesBrowserNode(VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super("Movies");
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
    }

    public List<BrowserModelNode> getChildren() {
        ArrayList<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
        BrowserModelNode allMoviesNode = new AllMoviesNode(videoPlayer, videoRepository);
        allMoviesNode.setParent(this);
        children.add(allMoviesNode);

        BrowserModelNode lastWeeksMoviesNode = new NewMoviesNode(7, videoPlayer, videoRepository);
        lastWeeksMoviesNode.setParent(this);
        children.add(lastWeeksMoviesNode);

        BrowserModelNode lastMonthMoviesNode = new NewMoviesNode(30, videoPlayer, videoRepository);
        lastMonthMoviesNode.setParent(this);
        children.add(lastMonthMoviesNode);

        for (Iterator<String> i = videoRepository.getTags().iterator(); i.hasNext();) {
            String tag = i.next();
            MovieTagNode movieTagNode = new MovieTagNode(tag, videoPlayer, videoRepository);
            movieTagNode.setParent(this);
            children.add(movieTagNode);
        }

        children.addAll(super.getChildren());
        return children;
    }
}
