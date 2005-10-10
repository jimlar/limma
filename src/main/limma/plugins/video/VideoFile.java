package limma.plugins.video;



public class VideoFile {
    private long id;
    private Video video;
    private String path;

    public VideoFile() {
    }

    public VideoFile(Video video, String path) {
        this.video = video;
        this.path = path;
    }

    public Video getVideo() {
        return video;
    }

    public String getPath() {
        return path;
    }

    public String toString() {
        return path;
    }
}
