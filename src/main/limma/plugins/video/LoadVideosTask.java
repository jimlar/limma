package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.navigationlist.DefaultNavigationNode;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

class LoadVideosTask implements Task {
    private PersistenceManager persistenceManager;
    private DefaultNavigationNode moviesNode;
    private VideoPlayer videoPlayer;
    private DialogManager dialogManager;

    public LoadVideosTask(PersistenceManager persistenceManager, DefaultNavigationNode moviesNode, VideoPlayer videoPlayer, DialogManager dialogManager) {
        this.persistenceManager = persistenceManager;
        this.moviesNode = moviesNode;
        this.videoPlayer = videoPlayer;
        this.dialogManager = dialogManager;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading video list...");
    }

    public void run() {
        List videos = persistenceManager.query("all_videos");

        for (Iterator i = videos.iterator(); i.hasNext();) {
            Video video = (Video) i.next();
            moviesNode.add(new MovieNavigationNode(video, videoPlayer, dialogManager));
        }
        moviesNode.sortByTitle();
    }
}
