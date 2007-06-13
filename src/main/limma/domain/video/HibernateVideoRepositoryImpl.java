package limma.domain.video;

import java.util.*;

import limma.persistence.PersistenceManager;

public class HibernateVideoRepositoryImpl implements VideoRepository {
    private PersistenceManager persistenceManager;


    public HibernateVideoRepositoryImpl(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
        persistenceManager.addPersistentClass(Video.class);
    }

    public void save(Video video) {
        persistenceManager.save(video);
    }

    public Video add(Video video) {
        return (Video) persistenceManager.create(video);
    }

    public void add(VideoFile videoFile) {
        persistenceManager.create(videoFile);
    }

    public void remove(VideoFile videoFile) {
        persistenceManager.delete(videoFile);
    }

    public void remove(Video video) {
        persistenceManager.delete(video);
    }

    public List<Video> getAllVideos() {
        List<Video> videos = new ArrayList<Video>(persistenceManager.loadAll(Video.class));
        Collections.sort(videos, new VideoComparator());
        return videos;
    }

    public List<Video> getVideosWithTag(String tag) {
        ArrayList<Video> result = new ArrayList<Video>();
        for (Iterator i = getAllVideos().iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            if (video.getTags().contains(tag)) {
                result.add(video);
            }
        }
        Collections.sort(result, new VideoComparator());
        return result;
    }

    public List<VideoFile> getAllVideoFiles() {
        return persistenceManager.loadAll(VideoFile.class);
    }


    private static class VideoComparator implements Comparator<Video> {
        public int compare(Video v1, Video v2) {
            return v1.getTitle().compareToIgnoreCase(v2.getTitle());
        }
    }
}
