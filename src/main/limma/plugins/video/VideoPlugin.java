package limma.plugins.video;

import limma.PlayerManager;
import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigationlist.MenuNode;
import limma.swing.navigationlist.NavigationList;
import limma.swing.navigationlist.NavigationModel;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private VideoPlayer videoPlayer;
    private MenuNode moviesNode;
    private MenuNode titlesNode;
    private MenuNode tagsNode;
    private UIProperties uiProperties;

    public VideoPlugin(final DialogManager dialogManager, PersistenceManager persistenceManager, IMDBSevice imdbSevice, final VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, NavigationList navigationList, PlayerManager playerManager, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;
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

        navigationList.addCellRenderer(new VideoListCellRenderer(videoConfig, uiProperties));
    }

    public void init() {
        navigationModel.add(moviesNode);
        reloadVideos();
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig, uiProperties));
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(persistenceManager, titlesNode, tagsNode, videoPlayer, dialogManager, imdbSevice, videoConfig, uiProperties));
    }
}
