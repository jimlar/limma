package limma.plugins.video;

import limma.domain.video.VideoRepository;
import limma.swing.Task;
import limma.swing.TaskFeedback;

class ScanForVideosTask implements Task {
    private VideoRepository videoRepository;

    public ScanForVideosTask(VideoRepository videoRepository) {
        this.videoRepository = videoRepository;
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Searching for videos...");
        videoRepository.scanForVideos();
    }
}
