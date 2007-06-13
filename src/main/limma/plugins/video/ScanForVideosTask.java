package limma.plugins.video;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import limma.domain.video.Video;
import limma.domain.video.VideoFile;
import limma.domain.video.VideoRepository;
import limma.swing.Task;
import limma.swing.TaskFeedback;
import limma.utils.DirectoryScanner;
import org.apache.commons.lang.StringUtils;

class ScanForVideosTask implements Task {
    private VideoPlugin videoPlugin;
    private VideoConfig videoConfig;
    private VideoRepository videoRepository;
    private Collection videoFileExtensions;

    public ScanForVideosTask(VideoPlugin videoPlugin, VideoConfig videoConfig, VideoRepository videoRepository) {
        this.videoPlugin = videoPlugin;
        this.videoConfig = videoConfig;
        this.videoRepository = videoRepository;
        this.videoFileExtensions = videoConfig.getVideoFileExtensions();
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Searching for videos...");
        List<File> moviesFiles = new ArrayList<File>();
        List<File> dvdDirectories = new ArrayList<File>();

        scanForDiskFiles(moviesFiles, dvdDirectories);

        deleteVideosNoLongerOnDisk();

        List<File> persistentFiles = getPersistentFiles();

        updateDvdDirectories(dvdDirectories, persistentFiles);

        updateFileVideos(moviesFiles, persistentFiles);

        videoPlugin.reloadVideos();
    }

    private void updateFileVideos(List<File> moviesFiles, List<File> persistentFiles) {
        for (Iterator<File> i = moviesFiles.iterator(); i.hasNext();) {
            File movieFile = i.next();
            if (!persistentFiles.contains(movieFile)) {
                System.out.println("Adding movie " + movieFile);

                Video video = new Video(guessNameFromFile(movieFile));
                video = (Video) videoRepository.add(video);
                VideoFile videoFile = new VideoFile(video, movieFile.getAbsolutePath());
                videoRepository.add(videoFile);
                persistentFiles.add(movieFile);

                List<File> similarFiles = findSimilarFiles(movieFile, moviesFiles, videoConfig.getSimilarFileDistance());
                for (Iterator j = similarFiles.iterator(); j.hasNext();) {
                    File similarFile = (File) j.next();
                    System.out.println(" - found similar file " + similarFile);
                    if (!persistentFiles.contains(similarFile)) {
                        videoFile = new VideoFile(video, similarFile.getAbsolutePath());
                    }
                    videoRepository.add(videoFile);
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

    private List<File> getPersistentFiles() {
        List videoFiles = videoRepository.getAllVideoFiles();
        ArrayList<File> persistentFiles = new ArrayList<File>();
        for (Iterator i = videoFiles.iterator(); i.hasNext();) {
            VideoFile videoFile = (VideoFile) i.next();
            persistentFiles.add(videoFile.getFile());
        }
        return persistentFiles;
    }

    private void updateDvdDirectories(List<File> dvdFiles, List<File> persistentFiles) {
        for (Iterator<File> i = dvdFiles.iterator(); i.hasNext();) {
            File dvdFile = i.next();

            if (!persistentFiles.contains(dvdFile)) {
                System.out.println("Adding DVD directory: " + dvdFile);

                Video video = new Video(guessNameFromFile(dvdFile));
                video = (Video) videoRepository.add(video);
                VideoFile videoFile = new VideoFile(video, dvdFile.getAbsolutePath());
                videoRepository.add(videoFile);
            }
        }
    }

    private void deleteVideosNoLongerOnDisk() {
        List<VideoFile> videoFiles = videoRepository.getAllVideoFiles();
        for (Iterator i = videoFiles.iterator(); i.hasNext();) {
            VideoFile videoFile = (VideoFile) i.next();
            Video video = videoFile.getVideo();

            if (!videoFile.getFile().exists()) {

                System.out.println("Deleting video file " + videoFile.getFile());
                videoRepository.remove(videoFile);
                video.getFiles().remove(videoFile);
                if (video.getFiles().isEmpty()) {
                    System.out.println(" - Video now empty, deletig video " + video.getTitle());
                    videoRepository.remove(video);
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
