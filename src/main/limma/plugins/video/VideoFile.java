package limma.plugins.video;

import java.io.File;


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

    public File getFile() {
        return new File(getPath());
    }

    public String getName() {
        return getFile().getName();
    }
}
