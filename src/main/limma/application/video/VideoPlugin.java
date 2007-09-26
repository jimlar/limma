package limma.application.video;

import limma.PlayerManager;
import limma.application.Plugin;
import limma.domain.video.VideoRepository;
import limma.ui.UIProperties;
import limma.ui.browser.MenuItem;
import limma.ui.browser.Navigation;
import limma.ui.browser.NavigationModel;
import limma.ui.dialogs.DialogManager;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private NavigationModel navigationModel;
    private VideoRepository videoRepository;
    private MoviesNavigationNode moviesNode;

    public VideoPlugin(final DialogManager dialogManager, VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, Navigation navigation, PlayerManager playerManager, UIProperties uiProperties, VideoRepository videoRepository) {
        this.dialogManager = dialogManager;
        this.navigationModel = navigationModel;
        this.videoRepository = videoRepository;

        moviesNode = new MoviesNavigationNode(videoPlayer, videoRepository);
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
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(videoRepository));
    }
}