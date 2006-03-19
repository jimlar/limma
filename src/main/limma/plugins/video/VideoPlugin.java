package limma.plugins.video;

import limma.PlayerManager;
import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.NavigationModel;
import limma.swing.menu.SimpleNavigationNode;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private IMDBService imdbService;
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private VideoPlayer videoPlayer;
    private MoviesNavigationNode moviesNode;
    private UIProperties uiProperties;
    private MovieStorage movieStorage;

    public VideoPlugin(final DialogManager dialogManager, PersistenceManager persistenceManager, IMDBService imdbService, final VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, limma.swing.menu.Navigation navigation, PlayerManager playerManager, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.imdbService = imdbService;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;
        this.videoPlayer = videoPlayer;
        this.uiProperties = uiProperties;

        persistenceManager.addPersistentClass(Video.class);

        movieStorage = new MovieStorage(persistenceManager);
        MovieNodeFactory movieNodeFactory = new MovieNodeFactory(movieStorage, videoPlayer, dialogManager, uiProperties, imdbService, persistenceManager, videoConfig);
        moviesNode = new MoviesNavigationNode(movieStorage, movieNodeFactory);
        moviesNode.addTrailingNode(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager, uiProperties));

        SimpleNavigationNode settingsNode = new SimpleNavigationNode("Settings");
        moviesNode.addTrailingNode(settingsNode);

        settingsNode.add(new SimpleNavigationNode("Scan for new videos") {
            public void performAction() {
                scanForVideos();
            }
        });

        navigation.addCellRenderer(new VideoNavigationNodeRenderer(videoConfig, uiProperties));
    }

    public void init() {
        navigationModel.add(moviesNode);
        reloadVideos();
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig, uiProperties));
    }

    public void reloadVideos() {
        SimpleNavigationNode titlesNode = null;
        SimpleNavigationNode tagsNode = null;
        dialogManager.executeInDialog(new LoadVideosTask(movieStorage, uiProperties));
    }
}
