package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.swing.navigationlist.NavigationList;
import limma.swing.navigationlist.NavigationModel;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private VideoPlayer videoPlayer;
    private DefaultNavigationNode moviesNode;
    private DefaultNavigationNode titlesNode;

    public VideoPlugin(DialogManager dialogManager, PersistenceManager persistenceManager, IMDBSevice imdbSevice, VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, NavigationList navigationList) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;
        this.videoPlayer = videoPlayer;

        persistenceManager.addPersistentClass(Video.class);

        moviesNode = new DefaultNavigationNode("Movies");
        titlesNode = new DefaultNavigationNode("Titles");
        moviesNode.add(titlesNode);

        DefaultNavigationNode settingsNode = new DefaultNavigationNode("Settings");
        moviesNode.add(settingsNode);

        settingsNode.add(new DefaultNavigationNode("Scan for new videos") {
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
        dialogManager.executeInDialog(new LoadVideosTask(persistenceManager, titlesNode, videoPlayer, dialogManager, imdbSevice, videoConfig));
    }
}
