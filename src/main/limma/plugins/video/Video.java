package limma.plugins.video;



public class Video {
    private long id;
    private String path;

    public Video() {
    }

    public Video(String path) {
        this.path = path;
    }

    public String toString() {
        return path;
    }
}
