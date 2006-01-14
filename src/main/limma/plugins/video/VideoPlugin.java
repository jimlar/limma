package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigationlist.NavigationList;
import limma.swing.navigationlist.NavigationModel;
import limma.swing.navigationlist.NavigationNode;
import limma.PlayerManager;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private VideoPlayer videoPlayer;
    private NavigationNode moviesNode;
    private NavigationNode titlesNode;
    private NavigationNode categoriesNode;

    public VideoPlugin(final DialogManager dialogManager, PersistenceManager persistenceManager, IMDBSevice imdbSevice, final VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, NavigationList navigationList, PlayerManager playerManager) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;
        this.videoPlayer = videoPlayer;

        persistenceManager.addPersistentClass(Video.class);

        moviesNode = new NavigationNode("Movies");
        titlesNode = new NavigationNode("All Movies");
        categoriesNode = new NavigationNode("Categories");
        moviesNode.add(titlesNode);
        moviesNode.add(categoriesNode);
        moviesNode.add(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager));

        NavigationNode settingsNode = new NavigationNode("Settings");
        moviesNode.add(settingsNode);

        settingsNode.add(new NavigationNode("Manage Categories"));
        settingsNode.add(new NavigationNode("Scan for new videos") {
            public void performAction() {
                scanForVideos();
            }
        });

        navigationList.addCellRenderer(new VideoListCellRenderer(videoConfig));
    }

    public void init() {
        navigationModel.add(moviesNode);
        reloadVideos();
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig));
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(persistenceManager, titlesNode, categoriesNode, videoPlayer, dialogManager, imdbSevice, videoConfig));
    }
}
