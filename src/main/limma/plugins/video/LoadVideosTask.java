package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.Task;

import javax.swing.*;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

class LoadVideosTask implements Task {
    private VideoPlugin videoPlugin;
    private PersistenceManager persistenceManager;

    public LoadVideosTask(VideoPlugin videoPlugin, PersistenceManager persistenceManager) {
        this.videoPlugin = videoPlugin;
        this.persistenceManager = persistenceManager;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading video list...");
    }

    public void run() {
        List videos = persistenceManager.query("all_videos");
        Collections.sort(videos, new Comparator() {
            public int compare(Object o1, Object o2) {
                Video video1 = (Video) o1;
                Video video2 = (Video) o2;
                return video1.getTitle().compareToIgnoreCase(video2.getTitle());
            }
        });
        videoPlugin.setVideos(videos);
    }
}
