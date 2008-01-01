package limma.ui.video;

import limma.application.PlayerManager;
import limma.application.Plugin;
import limma.application.video.VideoConfig;
import limma.application.video.VideoPlayer;
import limma.domain.video.VideoRepository;
import limma.ui.UIProperties;
import limma.ui.browser.Browser;
import limma.ui.browser.model.BrowserModel;
import limma.ui.browser.model.MenuItem;
import limma.ui.dialogs.DialogManager;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private BrowserModel browserModel;
    private VideoRepository videoRepository;
    private MoviesBrowserNode moviesNode;

    public VideoPlugin(final DialogManager dialogManager, VideoConfig videoConfig, VideoPlayer videoPlayer, BrowserModel browserModel, Browser browser, PlayerManager playerManager, UIProperties uiProperties, VideoRepository videoRepository) {
        this.dialogManager = dialogManager;
        this.browserModel = browserModel;
        this.videoRepository = videoRepository;

        moviesNode = new MoviesBrowserNode(videoPlayer, videoRepository);
        moviesNode.add(new PlayDVDDiscNode(dialogManager, videoConfig, playerManager));

        moviesNode.add(new MenuItem("Scan for new videos") {
            public void performAction(DialogManager dialogManager) {
                scanForVideos();
            }
        });

        //browser.addCellRenderer(new VideoBrowserNodeRenderer(videoConfig, uiProperties));
    }

    public void init() {
        browserModel.add(moviesNode);
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(videoRepository));
    }
}
