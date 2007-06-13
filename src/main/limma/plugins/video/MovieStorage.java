package limma.plugins.video;

import java.util.*;

import limma.domain.video.Video;
import limma.domain.video.VideoRepository;

public class MovieStorage {
    private VideoRepository videoRepository;
    private List<Video> videos = new ArrayList<Video>();

    public MovieStorage(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void refresh() {
        videos = videoRepository.getAllVideos();
    }

    public List<Video> getVideos() {
        return videos;
    }

    public List<String> getTags() {
        Set<String> tags = new HashSet<String>();
        for (Iterator i = videos.iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            tags.addAll(video.getTags());
        }

        ArrayList<String> result = new ArrayList<String>();
        result.addAll(tags);
        Collections.sort(result);
        return result;
    }

    public List<Video> getVideosWithTag(String tag) {
        return videoRepository.getVideosWithTag(tag);
    }
}
