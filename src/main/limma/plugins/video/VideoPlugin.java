package limma.plugins.video;

import limma.PlayerManager;
import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.MenuNode;
import limma.swing.menu.MenuModel;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private MenuModel menuModel;
    private VideoPlayer videoPlayer;
    private MenuNode moviesNode;
    private MenuNode titlesNode;
    private MenuNode tagsNode;
    private UIProperties uiProperties;

    public VideoPlugin(final DialogManager dialogManager, PersistenceManager persistenceManager, IMDBSevice imdbSevice, final VideoConfig videoConfig, VideoPlayer videoPlayer, MenuModel menuModel, limma.swing.menu.LimmaMenu limmaMenu, PlayerManager playerManager, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.menuModel = menuModel;
        this.videoPlayer = videoPlayer;
        this.uiProperties = uiProperties;

        persistenceManager.addPersistentClass(Video.class);

        moviesNode = new MenuNode("Movies");
        titlesNode = new MenuNode("All");
        tagsNode = new MenuNode("Tags");
        moviesNode.add(titlesNode);
        moviesNode.add(tagsNode);
        moviesNode.add(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager, uiProperties));

        MenuNode settingsNode = new MenuNode("Settings");
        moviesNode.add(settingsNode);

        settingsNode.add(new MenuNode("Scan for new videos") {
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
        dialogManager.executeInDialog(new LoadVideosTask(persistenceManager, titlesNode, tagsNode, videoPlayer, dialogManager, imdbSevice, videoConfig, uiProperties));
    }
}
