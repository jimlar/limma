package limma.ui.video;

import limma.application.PlayerManager;
import limma.application.Plugin;
import limma.application.video.IMDBService;
import limma.application.video.VideoConfig;
import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.UIProperties;
import limma.ui.browser.Browser;
import limma.ui.browser.model.BrowserModel;
import limma.ui.browser.model.MenuItem;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;


public class VideoPlugin implements Plugin {
    private DialogManager dialogManager;
    private BrowserModel browserModel;
    private VideoRepository videoRepository;
    private MoviesBrowserNode moviesNode;

    public VideoPlugin(final DialogManager dialogManager, final VideoConfig videoConfig, VideoPlayer videoPlayer, BrowserModel browserModel, PlayerManager playerManager, final VideoRepository videoRepository, final IMDBService imdbService, Browser browser, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.browserModel = browserModel;
        this.videoRepository = videoRepository;

        PlayDVDDiscNode dvdNode = new PlayDVDDiscNode(dialogManager, videoConfig, playerManager);
        moviesNode = new MoviesBrowserNode(videoPlayer, videoRepository, dvdNode);
        moviesNode.add(new MenuItem("Scan for new videos") {
            public void performAction(DialogManager dialogManager) {
                scanForVideos();
            }
        });
        moviesNode.add(new MenuItem("Update All Movies from IMDB") {
            public void performAction(DialogManager dialogManager) {

                dialogManager.executeInDialog(new Task() {
                    public void run(TaskFeedback feedback) {
                        Random random = new Random();
                        List<Video> videoList = videoRepository.getAllVideos();
                        for (ListIterator<Video> i = videoList.listIterator(); i.hasNext();) {
                            Video video = i.next();
                            feedback.setStatusMessage("Updating movie " + i.nextIndex() + " of " + videoList.size());
                            if (video.hasImdbNumber()) {
                                new UpdateFromImdbTask(imdbService, videoConfig, video, video.getImdbNumber(), videoRepository).run(new TaskFeedback() {
                                    public void setStatusMessage(String message) {
                                    }
                                });
                                try {
                                    Thread.sleep(random.nextInt(1000));
                                } catch (InterruptedException e) {
                                }
                            }
                        }
                    }
                });

            }
        });

//        browser.addCellRenderer(new VideoBrowserNodeRenderer(videoConfig, uiProperties));
    }

    public void init() {
        browserModel.add(moviesNode);
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(videoRepository, moviesNode));
    }
}
