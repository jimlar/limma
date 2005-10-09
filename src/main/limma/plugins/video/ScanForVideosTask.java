package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import java.io.File;

class ScanForVideosTask implements Task {
    private VideoPlugin videoPlugin;
    private PersistenceManager persistenceManager;

    public ScanForVideosTask(VideoPlugin videoPlugin, PersistenceManager persistenceManager) {
        this.videoPlugin = videoPlugin;
        this.persistenceManager = persistenceManager;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Searching for videos...");
    }

    public void run() {
        persistenceManager.deleteAll(Video.class);
        new DirectoryScanner(new File("/media/movies")).accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                Video video = createVideoIfPossible(file);
                if (video != null) {
                    persistenceManager.create(video);
                }
                return video == null;
            }
        });
        videoPlugin.reloadVideos();
    }

    private Video createVideoIfPossible(File file) {
        String name = file.getName();
        if (name.endsWith(".avi") || name.endsWith(".mpg") || name.endsWith(".img")) {
            return new Video(file.getAbsolutePath());
        }
        if (new File(file, "VIDEO_TS.IFO").isFile()) {
            return new Video(file.getAbsolutePath());
        }
        return null;
    }
}
