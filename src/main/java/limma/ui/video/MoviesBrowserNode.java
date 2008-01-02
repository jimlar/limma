package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.ArrayList;
import java.util.List;

public class MoviesBrowserNode extends SimpleBrowserNode {
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;
    private BrowserModelNode allMoviesNode;
    private BrowserModelNode lastWeeksMoviesNode;
    private BrowserModelNode lastMonthMoviesNode;

    public MoviesBrowserNode(VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super("Movies");
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;

        allMoviesNode = new AllMoviesNode(videoPlayer, videoRepository);
        allMoviesNode.setParent(this);

        lastWeeksMoviesNode = new NewMoviesNode(7, videoPlayer, videoRepository);
        lastWeeksMoviesNode.setParent(this);

        lastMonthMoviesNode = new NewMoviesNode(30, videoPlayer, videoRepository);
        lastMonthMoviesNode.setParent(this);
    }

    public List<BrowserModelNode> getChildren() {
        ArrayList<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
        children.add(allMoviesNode);
        children.add(lastWeeksMoviesNode);
        children.add(lastMonthMoviesNode);

        for (String tag : videoRepository.getTags()) {
            MovieTagNode movieTagNode = new MovieTagNode(tag, videoPlayer, videoRepository);
            movieTagNode.setParent(this);
            children.add(movieTagNode);
        }

        children.addAll(super.getChildren());
        return children;
    }
}
