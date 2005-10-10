package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.utils.DirectoryScanner;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class ScanForVideosTask implements Task {
    private static final List SINGLE_FILE_VIDEO_EXTENSIONS = Arrays.asList(new String[]{"avi",
                                                                                        "mpg",
                                                                                        "mpeg",
                                                                                        "img",
                                                                                        "mkv",
                                                                                        "nrg"});
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
        final DirectoryScanner directoryScanner = new DirectoryScanner(new File("/media/movies"), true);
        directoryScanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {

                if (isMovieFile(file)) {
                    Video video = new Video(guessNameFromFile(file));
                    persistenceManager.create(video);
                    persistenceManager.create(new VideoFile(video, file.getAbsolutePath()));

                    List similarFiles = findSimilarFiles(file);
                    for (Iterator i = similarFiles.iterator(); i.hasNext();) {
                        File similarFile = (File) i.next();
                        persistenceManager.create(new VideoFile(video, similarFile.getAbsolutePath()));
                        directoryScanner.skip(similarFile);
                    }
                    return false;

                } else if (isDVD(file)) {
                    Video video = new Video(guessNameFromFile(file));
                    persistenceManager.create(video);
                    persistenceManager.create(new VideoFile(video, file.getAbsolutePath()));
                    return false;

                } else {
                    return true;
                }
            }
        });
        videoPlugin.reloadVideos();
    }

    private List findSimilarFiles(File file) {
        final List similarFiles = new ArrayList();
        new DirectoryScanner(file.getParentFile()).accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isMovieFile(file)) {
                    similarFiles.add(file);
                }
                return true;
            }
        });

        for (Iterator i = similarFiles.iterator(); i.hasNext();) {
            File candidate = (File) i.next();
            int levenshteinDistance = StringUtils.getLevenshteinDistance(file.getAbsolutePath(), candidate.getAbsolutePath());
            if (levenshteinDistance != 1 && levenshteinDistance != 2) {
                i.remove();
            }
        }
        return similarFiles;
    }

    private boolean isMovieFile(File file) {
        String name = file.getName();
        int i = name.lastIndexOf('.');
        if (i == -1) {
            return false;
        }
        String extension = name.substring(i + 1).toLowerCase();
        return SINGLE_FILE_VIDEO_EXTENSIONS.contains(extension);
    }

    private boolean isDVD(File file) {
        return new File(file, "VIDEO_TS.IFO").isFile();
    }

    private String guessNameFromFile(File file) {
        String name = file.getName();
        if (name.equalsIgnoreCase("VIDEO_TS")) {
            name = file.getParentFile().getName();
        }
        int endIndex = name.lastIndexOf('.');
        if (endIndex != -1) {
            name = name.substring(0, endIndex);
        }
        name = name.replace('_', ' ');
        return name;
    }
}
