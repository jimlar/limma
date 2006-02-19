package limma.plugins.video;

import limma.persistence.PersistenceManager;

import java.util.*;

public class MovieStorage {
    private PersistenceManager persistenceManager;
    private List videos = new ArrayList();

    public MovieStorage(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void refresh() {
        videos = persistenceManager.query("all_videos");
    }

    public List getVideos() {
        return videos;
    }

    public Set<String> getTags() {
        Set <String> tags = new HashSet<String>();
        for (Iterator i = videos.iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            tags.addAll(video.getTags());
        }
        return tags;
    }

    public List<Video> getVideosWithTag(String tag) {
        ArrayList<Video> result = new ArrayList<Video>();
        for (Iterator i = this.videos.iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            if (video.getTags().contains(tag)) {
                result.add(video);
            }
        }
        return result;
    }
}
