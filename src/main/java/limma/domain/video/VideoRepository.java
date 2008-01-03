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

    Set<Video> getVideosOfGenre(String genre);

    Set<String> getAllGenres();

    Set<Video> getVideosWithActor(String actor);

    Set<String> getAllActors();

    Set<Video> getVideosWithDirector(String director);

    Set<String> getAllDirectors();
}
