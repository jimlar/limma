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
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private MoviesNavigationNode moviesNode;
    private MovieStorage movieStorage;

    public VideoPlugin(final DialogManager dialogManager, PersistenceManager persistenceManager, VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, limma.swing.menu.Navigation navigation, PlayerManager playerManager, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;

        persistenceManager.addPersistentClass(Video.class);

        movieStorage = new MovieStorage(persistenceManager);
        moviesNode = new MoviesNavigationNode(movieStorage, videoPlayer, persistenceManager);
        moviesNode.addTrailingNode(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager));

        SimpleNavigationNode settingsNode = new SimpleNavigationNode("Settings");
        moviesNode.addTrailingNode(settingsNode);

        settingsNode.add(new SimpleNavigationNode("Scan for new videos") {
            public void performAction(DialogManager dialogManager) {
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
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig));
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(movieStorage));
    }
}
