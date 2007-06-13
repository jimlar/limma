package limma.plugins.video;

import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.TaskFeedback;
import limma.swing.navigation.NavigationNode;

class ToggleTagNode extends NavigationNode {
    private String tag;
    private Video video;
    private VideoRepository videoRepository;

    public ToggleTagNode(String tag, Video video, VideoRepository videoRepository) {
        this.tag = tag;
        this.video = video;
        this.videoRepository = videoRepository;
    }

    public String getTitle() {
        return (video.getTags().contains(tag) ? "+ " : "- ") + tag;
    }

    public void performAction(DialogManager dialogManager) {
        dialogManager.executeInDialog(new ToggleTagTask(video, tag, videoRepository));
    }

    public static class ToggleTagTask implements Task {
        private Video video;
        private String tag;
        private VideoRepository videoRepository;

        public ToggleTagTask(Video video, String tag, VideoRepository videoRepository) {
            this.video = video;
            this.tag = tag;
            this.videoRepository = videoRepository;
        }

        public void run(TaskFeedback feedback) {
            feedback.setStatusMessage("Toggling tag");
            if (video.getTags().contains(tag)) {
                video.getTags().remove(tag);
            } else {
                video.getTags().add(tag);
            }
            videoRepository.save(video);
        }
    }
}
