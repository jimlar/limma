package limma.plugins.video;

import limma.PlayerManager;
import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.SimpleMenuNode;
import limma.swing.menu.MenuModel;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private IMDBService imdbService;
    private VideoConfig videoConfig;
    private MenuModel menuModel;
    private VideoPlayer videoPlayer;
    private MoviesMenuNode moviesNode;
    private UIProperties uiProperties;
    private MovieStorage movieStorage;

    public VideoPlugin(final DialogManager dialogManager, PersistenceManager persistenceManager, IMDBService imdbService, final VideoConfig videoConfig, VideoPlayer videoPlayer, MenuModel menuModel, limma.swing.menu.LimmaMenu limmaMenu, PlayerManager playerManager, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.imdbService = imdbService;
        this.videoConfig = videoConfig;
        this.menuModel = menuModel;
        this.videoPlayer = videoPlayer;
        this.uiProperties = uiProperties;

        persistenceManager.addPersistentClass(Video.class);

        movieStorage = new MovieStorage(persistenceManager);
        MovieNodeFactory movieNodeFactory = new MovieNodeFactory(movieStorage, videoPlayer, dialogManager, uiProperties, imdbService, persistenceManager, videoConfig);
        moviesNode = new MoviesMenuNode(movieStorage, movieNodeFactory);
        moviesNode.addTrailingNode(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager, uiProperties));

        SimpleMenuNode settingsNode = new SimpleMenuNode("Settings");
        moviesNode.addTrailingNode(settingsNode);

        settingsNode.add(new SimpleMenuNode("Scan for new videos") {
            public void performAction() {
                scanForVideos();
            }
        });

        limmaMenu.addCellRenderer(new VideoMenuCellRenderer(videoConfig, uiProperties));
    }

    public void init() {
        menuModel.add(moviesNode);
        reloadVideos();
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig, uiProperties));
    }

    public void reloadVideos() {
        SimpleMenuNode titlesNode = null;
        SimpleMenuNode tagsNode = null;
        dialogManager.executeInDialog(new LoadVideosTask(movieStorage, uiProperties));
    }
}
