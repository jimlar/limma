package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.Task;

import javax.swing.*;

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
        videoPlugin.setVideos(persistenceManager.loadAll(Video.class));
    }
}
