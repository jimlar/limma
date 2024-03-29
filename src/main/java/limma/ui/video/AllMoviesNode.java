package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.List;

public class AllMoviesNode extends SimpleBrowserNode {

    public AllMoviesNode(VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super("All");

        List<Video> allVideos = videoRepository.getAllVideos();
        for (Video video : allVideos) {
            add(new MovieBrowserNode(video, videoPlayer, videoRepository));
        }
    }
}
