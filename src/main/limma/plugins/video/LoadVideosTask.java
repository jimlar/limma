package limma.plugins.video;

import limma.swing.Task;
import limma.swing.TaskFeedback;

class LoadVideosTask implements Task {
    private MovieStorage movieStorage;

    public LoadVideosTask(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Loading movies...");
        movieStorage.refresh();
    }

}
