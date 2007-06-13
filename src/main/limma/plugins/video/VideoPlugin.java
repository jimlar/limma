package limma.plugins.video;

import limma.PlayerManager;
import limma.UIProperties;
import limma.domain.video.VideoRepository;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigation.MenuItem;
import limma.swing.navigation.NavigationModel;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private VideoRepository videoRepository;
    private MoviesNavigationNode moviesNode;
    private MovieStorage movieStorage;

    public VideoPlugin(final DialogManager dialogManager, VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, limma.swing.navigation.Navigation navigation, PlayerManager playerManager, UIProperties uiProperties, VideoRepository videoRepository) {
        this.dialogManager = dialogManager;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;
        this.videoRepository = videoRepository;

        movieStorage = new MovieStorage(videoRepository);
        moviesNode = new MoviesNavigationNode(movieStorage, videoPlayer, videoRepository);
        moviesNode.add(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager));

        moviesNode.add(new MenuItem("Scan for new videos") {
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
        dialogManager.executeInDialog(new ScanForVideosTask(this, videoConfig, videoRepository));
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(movieStorage));
    }
}
