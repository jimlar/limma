package limma.ui.video;

import limma.domain.video.VideoRepository;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;

class ScanForVideosTask implements Task {
    private VideoRepository videoRepository;
    private MoviesBrowserNode moviesNode;

    public ScanForVideosTask(VideoRepository videoRepository, MoviesBrowserNode moviesNode) {
        this.videoRepository = videoRepository;
        this.moviesNode = moviesNode;
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Searching for videos...");
        videoRepository.scanForVideos();
        moviesNode.rebuild();
    }
}
