package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.navigationlist.NavigationNode;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;

class LoadVideosTask implements Task {
    private PersistenceManager persistenceManager;
    private NavigationNode titlesNode;
    private NavigationNode categoriesNode;
    private VideoPlayer videoPlayer;
    private DialogManager dialogManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;

    public LoadVideosTask(PersistenceManager persistenceManager, NavigationNode titlesNode, NavigationNode categoriesNode, VideoPlayer videoPlayer, DialogManager dialogManager, IMDBSevice imdbSevice, VideoConfig videoConfig) {
        this.persistenceManager = persistenceManager;
        this.titlesNode = titlesNode;
        this.categoriesNode = categoriesNode;
        this.videoPlayer = videoPlayer;
        this.dialogManager = dialogManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading movies...");
    }

    public void run() {
        List videos = persistenceManager.query("all_videos");

        titlesNode.removeAllChildren();
        for (Iterator i = videos.iterator(); i.hasNext();) {
            final Video video = (Video) i.next();
            MovieNavigationNode movieNode = new MovieNavigationNode(video, videoPlayer, dialogManager);
            titlesNode.add(movieNode);

            movieNode.add(new NavigationNode("Update from IMDB") {
                public void performAction() {
                    IMDBDialog imdbDialog = new IMDBDialog(dialogManager, video, imdbSevice, persistenceManager, videoConfig);
                    imdbDialog.open();
                }
            });
        }
        titlesNode.sortByTitle();

        categoriesNode.removeAllChildren();
        categoriesNode.add(new NavigationNode("Uncategorized"));
        categoriesNode.sortByTitle();
    }
}
