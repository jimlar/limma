package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.List;

public class MovieTagNode extends SimpleBrowserNode {

    public MovieTagNode(String tag, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super(tag);

        List<Video> videosWithTag = videoRepository.getVideosWithTag(tag);
        for (Video video : videosWithTag) {
            add(new MovieBrowserNode(video, videoPlayer, videoRepository));
        }
    }
}
