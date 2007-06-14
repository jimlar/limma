package limma.domain.video;

import java.io.File;


public class VideoFile {
    private long id;
    private Video video;
    private String path;


    public VideoFile() {
    }

    public VideoFile(String path) {
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

    public File getFile() {
        return new File(getPath());
    }

    public String getName() {
        return getFile().getName();
    }

    void setVideo(Video video) {
        this.video = video;
    }
}
