package limma.plugins.video;

import java.util.HashSet;
import java.util.Set;


public class Video {
    private long id;
    private String name;
    private boolean dvd;
    private Set files = new HashSet();

    public Video() {
    }

    public Video(String name, boolean isDvd) {
        this.name = name;
        dvd = isDvd;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public boolean isDvd() {
        return dvd;
    }

    public Set getFiles() {
        return files;
    }
}
