package limma.plugins.video;

import limma.UIProperties;
import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.swing.TaskInfo;

import javax.swing.*;

class LoadVideosTask implements Task {
    private UIProperties uiProperties;
    private MovieStorage movieStorage;

    public LoadVideosTask(MovieStorage movieStorage, UIProperties uiProperties) {
        this.movieStorage = movieStorage;
        this.uiProperties = uiProperties;
    }

    public JComponent prepareToRun(TaskInfo taskInfo) {
        return new AntialiasLabel("Loading movies...", uiProperties);
    }

    public void run() {
        movieStorage.refresh();
    }

}
