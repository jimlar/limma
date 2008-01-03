package limma.domain.video;

import limma.application.video.VideoConfig;
import limma.domain.AbstractXMLRepository;
import limma.utils.DirectoryScanner;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.*;

public class XMLVideoRepositoryImpl extends AbstractXMLRepository implements VideoRepository {
    private VideoConfig videoConfig;
    private List<Video> videos = new ArrayList<Video>();
    private Date lastUpdated = new Date();


    public XMLVideoRepositoryImpl(VideoConfig videoConfig) {
        super(videoConfig.getMetaDataFile());
        this.videoConfig = videoConfig;
        addXmlAlias("video", Video.class);
        addXmlAlias("file", VideoFile.class);
        addXmlAlias("video-metadata", VideoList.class);
        reReadVideoMetaData();
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public Set<Video> getVideosOfGenre(String genre) {
        Set<Video> result = new HashSet<Video>();
        for (Video video : videos) {
            if (video.getGenres().contains(genre)) {
                result.add(video);
            }
        }
        return result;
    }

    public Set<String> getAllGenres() {
        Set<String> result = new HashSet<String>();
        for (Video video : getAllVideos()) {
            result.addAll(video.getGenres());
        }
        return result;
    }

    public Set<Video> getVideosWithActor(String actor) {
        Set<Video> result = new HashSet<Video>();
        for (Video video : videos) {
            if (video.getActors().contains(actor)) {
                result.add(video);
            }
        }
        return result;
    }

    public Set<String> getAllActors() {
        Set<String> result = new HashSet<String>();
        for (Video video : getAllVideos()) {
            result.addAll(video.getActors());
        }
        return result;
    }

    public Set<Video> getVideosWithDirector(String director) {
        Set<Video> result = new HashSet<Video>();
        for (Video video : videos) {
            if (director != null && director.equals(video.getDirector())) {
                result.add(video);
            }
        }
        return result;
    }

    public Set<String> getAllDirectors() {
        Set<String> result = new HashSet<String>();
        for (Video video : getAllVideos()) {
            if (!StringUtils.isBlank(video.getDirector())){
                result.add(video.getDirector());
            }
        }
        return result;
    }

    public void save(Video video) {
        storeVideoMetaData();
    }

    public List<Video> getAllVideos() {
        return videos;
    }

    public List<Video> getVideosWithTag(String tag) {
        ArrayList<Video> result = new ArrayList<Video>();
        for (Video video : getAllVideos()) {
            if (video.getTags().contains(tag)) {
                result.add(video);
            }
        }
        Collections.sort(result, new VideoComparator());
        return result;
    }


    public void scanForVideos() {
        List<File> moviesFiles = new ArrayList<File>();
        List<File> dvdDirectories = new ArrayList<File>();

        scanForDiskFiles(moviesFiles, dvdDirectories);

        deleteVideosNoLongerOnDisk();

        List<File> persistentFiles = getPersistentFiles();

        updateDvdDirectories(dvdDirectories, persistentFiles);

        updateFileVideos(moviesFiles, persistentFiles);

        storeVideoMetaData();
    }

    public List<String> getTags() {
        Set<String> tags = new HashSet<String>();
        for (Object video1 : videos) {
            Video video = (Video) video1;
            tags.addAll(video.getTags());
        }

        ArrayList<String> result = new ArrayList<String>();
        result.addAll(tags);
        Collections.sort(result);
        return result;
    }


    private void storeVideoMetaData() {
        VideoList list = new VideoList();
        list.setVideos(videos);
        store(list);
    }

    private void reReadVideoMetaData() {
        VideoList videoList = (VideoList) load();
        if (videoList == null) {
            this.videos = new ArrayList<Video>();
        } else {
            this.videos = new ArrayList<Video>(videoList.getVideos());
        }
        Collections.sort(videos, new VideoComparator());
        lastUpdated = new Date();
    }

    private List<VideoFile> getAllVideoFiles() {
        ArrayList<VideoFile> videoFiles = new ArrayList<VideoFile>();
        for (Object video1 : videos) {
            Video video = (Video) video1;
            videoFiles.addAll(video.getFiles());
        }
        return videoFiles;
    }

    private void updateFileVideos(List<File> moviesFiles, List<File> persistentFiles) {
        for (File movieFile : moviesFiles) {
            if (!persistentFiles.contains(movieFile)) {
                System.out.println("Adding movie " + movieFile);

                Video video = new Video(guessNameFromFile(movieFile));
                videos.add(video);
                VideoFile videoFile = new VideoFile(movieFile.getAbsolutePath());
                video.add(videoFile);
                persistentFiles.add(movieFile);

                List<File> similarFiles = findSimilarFiles(movieFile, moviesFiles, videoConfig.getSimilarFileDistance());
                for (File similarFile : similarFiles) {
                    System.out.println(" - found similar file " + similarFile);
                    if (!persistentFiles.contains(similarFile)) {
                        videoFile = new VideoFile(similarFile.getAbsolutePath());
                    }
                    video.add(videoFile);
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
        List<VideoFile> videoFiles = getAllVideoFiles();
        ArrayList<File> persistentFiles = new ArrayList<File>();
        for (VideoFile videoFile : videoFiles) {
            persistentFiles.add(videoFile.getFile());
        }
        return persistentFiles;
    }

    private void updateDvdDirectories(List<File> dvdFiles, List<File> persistentFiles) {
        for (File dvdFile : dvdFiles) {
            if (!persistentFiles.contains(dvdFile)) {
                System.out.println("Adding DVD directory: " + dvdFile);

                Video video = new Video(guessNameFromFile(dvdFile));
                video.add(new VideoFile(dvdFile.getAbsolutePath()));
                videos.add(video);
            }
        }
    }

    private void deleteVideosNoLongerOnDisk() {
        List<VideoFile> videoFiles = getAllVideoFiles();
        for (Object videoFile1 : videoFiles) {
            VideoFile videoFile = (VideoFile) videoFile1;
            Video video = videoFile.getVideo();

            if (!videoFile.getFile().exists()) {

                System.out.println("Deleting video file " + videoFile.getFile());
                video.remove(videoFile);
                video.getFiles().remove(videoFile);
                if (video.getFiles().isEmpty()) {
                    System.out.println(" - Video now empty, deletig video " + video.getTitle());
                    videos.remove(video);
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
        return videoConfig.getVideoFileExtensions().contains(extension);
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

    private static class VideoComparator implements Comparator<Video> {
        public int compare(Video v1, Video v2) {
            return v1.getTitle().compareToIgnoreCase(v2.getTitle());
        }
    }

    public static class VideoList {
        private List<Video> videos;


        public List<Video> getVideos() {
            return videos;
        }

        public void setVideos(List<Video> videos) {
            this.videos = videos;
        }
    }
}
