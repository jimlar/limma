package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.Set;

public class GenreNode extends SimpleBrowserNode {
    public GenreNode(String genre, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super(genre);

        Set<Video> allVideos = videoRepository.getVideosOfGenre(genre);
        for (Video video : allVideos) {
            add(new MovieBrowserNode(video, videoPlayer, videoRepository));
        }
        sort();
    }
}
