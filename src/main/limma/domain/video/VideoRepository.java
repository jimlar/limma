package limma.domain.video;

import java.util.List;

public interface VideoRepository {
    void save(Video video);

    Video add(Video video);

    void add(VideoFile videoFile);

    void remove(VideoFile videoFile);

    void remove(Video video);

    List<Video> getAllVideos();

    List<Video> getVideosWithTag(String tag);

    List<VideoFile> getAllVideoFiles();
}
