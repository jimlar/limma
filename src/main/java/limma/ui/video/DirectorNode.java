package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.Set;

public class DirectorNode extends SimpleBrowserNode {
    public DirectorNode(String director, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super(director);

        Set<Video> videos = videoRepository.getVideosWithDirector(director);
        for (Video video : videos) {
            add(new MovieBrowserNode(video, videoPlayer, videoRepository));
        }
        sort();
    }
}
