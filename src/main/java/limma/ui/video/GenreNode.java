package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.List;

public class GenreNode extends SimpleBrowserNode {
    public GenreNode(String genre, VideoPlayer videoPlayer, VideoRepository videoRepository) {
        super(genre);

        List<Video> allVideos = videoRepository.getVideosOfGenre(genre);
        for (Video video : allVideos) {
            add(new MovieBrowserNode(video, videoPlayer, videoRepository));
        }
        sortByTitle();
    }
}
