package limma.domain.video;

import java.util.Date;
import java.util.List;
import java.util.Set;

public interface VideoRepository {

    void save(Video video);

    List<Video> getAllVideos();

    List<Video> getVideosWithTag(String tag);

    void scanForVideos();

    List<String> getTags();

    Date getLastUpdated();

    List<Video> getVideosOfGenre(String genre);

    Set<String> getAllGenres();

    List<Video> getVideosWithActor(String actor);

    Set<String> getAllActors();
}
