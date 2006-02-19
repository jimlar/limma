package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.menu.SimpleMenuNode;
import limma.UIProperties;

import javax.swing.*;

class LoadVideosTask implements Task {
    private PersistenceManager persistenceManager;
    private SimpleMenuNode titlesNode;
    private SimpleMenuNode tagsNode;
    private VideoPlayer videoPlayer;
    private DialogManager dialogManager;
    private IMDBService imdbService;
    private VideoConfig videoConfig;
    private UIProperties uiProperties;
    private MovieStorage movieStorage;

    public LoadVideosTask(PersistenceManager persistenceManager, MovieStorage movieStorage, SimpleMenuNode titlesNode, SimpleMenuNode categoriesNode, VideoPlayer videoPlayer, DialogManager dialogManager, IMDBService imdbService, VideoConfig videoConfig, UIProperties uiProperties) {
        this.persistenceManager = persistenceManager;
        this.movieStorage = movieStorage;
        this.titlesNode = titlesNode;
        this.tagsNode = categoriesNode;
        this.videoPlayer = videoPlayer;
        this.dialogManager = dialogManager;
        this.imdbService = imdbService;
        this.videoConfig = videoConfig;
        this.uiProperties = uiProperties;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading movies...", uiProperties);
    }

    public void run() {
        movieStorage.refresh();
    }

}
