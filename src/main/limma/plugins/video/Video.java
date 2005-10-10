package limma.plugins.video;

import java.util.HashSet;
import java.util.Set;


public class Video {
    private long id;
    private String name;
    private Set files = new HashSet();

    public Video() {
    }

    public Video(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public Set getFiles() {
        return files;
    }
}
