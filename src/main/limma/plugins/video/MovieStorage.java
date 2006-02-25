package limma.plugins.video;

import limma.persistence.PersistenceManager;

import java.util.*;

public class MovieStorage {
    private PersistenceManager persistenceManager;
    private List<Video> videos = new ArrayList<Video>();

    public MovieStorage(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public void refresh() {
        videos = new ArrayList<Video>(persistenceManager.query("all_videos"));
        Collections.sort(videos, new VideoComparator());
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
        ArrayList<Video> result = new ArrayList<Video>();
        for (Iterator i = this.videos.iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            if (video.getTags().contains(tag)) {
                result.add(video);
            }
        }
        Collections.sort(result, new VideoComparator());
        return result;
    }

    private static class VideoComparator implements Comparator<Video> {
        public int compare(Video v1, Video v2) {
            return v1.getTitle().compareToIgnoreCase(v2.getTitle());
        }
    }
}
