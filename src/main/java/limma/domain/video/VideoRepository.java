package limma.domain.video;

import java.util.Date;
import java.util.List;

public interface VideoRepository {

    void save(Video video);

    List<Video> getAllVideos();

    List<Video> getVideosWithTag(String tag);

    void scanForVideos();

    List<String> getTags();

    Date getLastUpdated();
}
