package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.Set;

public class ActorNode extends SimpleBrowserNode {
    public ActorNode(String actor, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super(actor);

        Set<Video> videos = videoRepository.getVideosWithActor(actor);
        for (Video video : videos) {
            add(new MovieBrowserNode(video, videoPlayer, videoRepository));
        }
        sortByTitle();
    }
}
