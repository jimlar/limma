package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.TaskFeedback;
import limma.swing.TransactionalTask;
import limma.utils.DirectoryScanner;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class ScanForVideosTask extends TransactionalTask {
    private VideoPlugin videoPlugin;
    private VideoConfig videoConfig;
    private Collection videoFileExtensions;

    public ScanForVideosTask(VideoPlugin videoPlugin, PersistenceManager persistenceManager, VideoConfig videoConfig) {
        super(persistenceManager);
        this.videoPlugin = videoPlugin;
        this.videoConfig = videoConfig;
        this.videoFileExtensions = videoConfig.getVideoFileExtensions();
    }

    public void runInTransaction(TaskFeedback feedback, Session session) {
        feedback.setStatusMessage("Searching for videos...");
        List<File> moviesFiles = new ArrayList<File>();
        List<File> dvdDirectories = new ArrayList<File>();

        scanForDiskFiles(moviesFiles, dvdDirectories);

        deleteVideosNoLongerOnDisk(session);

        List<File> persistentFiles = getPersistentFiles(session);

        updateDvdDirectories(dvdDirectories, persistentFiles, session);

        updateFileVideos(moviesFiles, persistentFiles, session);

        videoPlugin.reloadVideos();
    }

    private void updateFileVideos(List<File> moviesFiles, List<File> persistentFiles, Session session) {
        for (Iterator<File> i = moviesFiles.iterator(); i.hasNext();) {
            File movieFile = i.next();
            if (!persistentFiles.contains(movieFile)) {
                System.out.println("Adding movie " + movieFile);

                Video video = new Video(guessNameFromFile(movieFile));
                video = (Video) session.get(Video.class, session.save(video));
                VideoFile videoFile = new VideoFile(video, movieFile.getAbsolutePath());
                session.save(videoFile);
                persistentFiles.add(movieFile);

                List<File> similarFiles = findSimilarFiles(movieFile, moviesFiles, videoConfig.getSimilarFileDistance());
                for (Iterator j = similarFiles.iterator(); j.hasNext();) {
                    File similarFile = (File) j.next();
                    System.out.println(" - found similar file " + similarFile);
                    if (!persistentFiles.contains(similarFile)) {
                        videoFile = new VideoFile(video, similarFile.getAbsolutePath());
                    }
                    session.save(videoFile);
                    persistentFiles.add(similarFile);
                }
            }
        }
    }

    private void scanForDiskFiles(final List<File> moviesFiles, final List<File> dvdDirectories) {
        final DirectoryScanner directoryScanner = new DirectoryScanner(videoConfig.getMovieDir(), true);
        directoryScanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isDVDDirectory(file)) {
                    dvdDirectories.add(file);
                    return false;
                }
                if (isMovieFile(file)) {
                    moviesFiles.add(file);
                    return false;
                }
                return true;
            }
        });
    }

    private List<File> getPersistentFiles(Session session) {
        List videoFiles = session.getNamedQuery("all_video_files").list();
        ArrayList<File> persistentFiles = new ArrayList<File>();
        for (Iterator i = videoFiles.iterator(); i.hasNext();) {
            VideoFile videoFile = (VideoFile) i.next();
            persistentFiles.add(videoFile.getFile());
        }
        return persistentFiles;
    }

    private void updateDvdDirectories(List<File> dvdFiles, List<File> persistentFiles, Session session) {
        for (Iterator<File> i = dvdFiles.iterator(); i.hasNext();) {
            File dvdFile = i.next();

            if (!persistentFiles.contains(dvdFile)) {
                System.out.println("Adding DVD directory: " + dvdFile);

                Video video = new Video(guessNameFromFile(dvdFile));
                video = (Video) session.get(Video.class, session.save(video));
                VideoFile videoFile = new VideoFile(video, dvdFile.getAbsolutePath());
                session.save(videoFile);
            }
        }
    }

    private void deleteVideosNoLongerOnDisk(Session session) {
        List videoFiles = session.getNamedQuery("all_video_files").list();
        for (Iterator i = videoFiles.iterator(); i.hasNext();) {
            VideoFile videoFile = (VideoFile) i.next();
            Video video = videoFile.getVideo();

            if (!videoFile.getFile().exists()) {

                System.out.println("Deleting video file " + videoFile.getFile());
                session.delete(videoFile);
                video.getFiles().remove(videoFile);
                if (video.getFiles().isEmpty()) {
                    System.out.println(" - Video now empty, deletig video " + video.getTitle());
                    session.delete(video);
                }
            }
        }
    }

    protected List<File> findSimilarFiles(File file, List<File> moviesFiles, int similarityDistance) {
        List<File> similarFiles = new ArrayList<File>(moviesFiles);

        for (Iterator i = similarFiles.iterator(); i.hasNext();) {
            File candidate = (File) i.next();
            int levenshteinDistance = StringUtils.getLevenshteinDistance(file.getAbsolutePath(), candidate.getAbsolutePath());
            if (levenshteinDistance == 0 || levenshteinDistance > similarityDistance) {
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
        return videoFileExtensions.contains(extension);
    }

    private boolean isDVDDirectory(File file) {
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
