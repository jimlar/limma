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
    private DefaultNavigationNode titlesNode;
    private VideoPlayer videoPlayer;
    private DialogManager dialogManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;

    public LoadVideosTask(PersistenceManager persistenceManager, DefaultNavigationNode titlesNode, VideoPlayer videoPlayer, DialogManager dialogManager, IMDBSevice imdbSevice, VideoConfig videoConfig) {
        this.persistenceManager = persistenceManager;
        this.titlesNode = titlesNode;
        this.videoPlayer = videoPlayer;
        this.dialogManager = dialogManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading video list...");
    }

    public void run() {
        List videos = persistenceManager.query("all_videos");

        titlesNode.removeAllChildren();
        for (Iterator i = videos.iterator(); i.hasNext();) {
            final Video video = (Video) i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, dialogManager);
            titlesNode.add(movieNode);

            movieNode.add(new DefaultNavigationNode("Update from IMDB") {
                public void performAction() {
                    IMDBDialog imdbDialog = new IMDBDialog(dialogManager, video, imdbSevice, persistenceManager, videoConfig);
                    imdbDialog.open();
                }
            });
        }
        titlesNode.sortByTitle();
    }
}
