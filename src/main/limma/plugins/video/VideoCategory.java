package limma.plugins.video;

import java.util.Set;
import java.util.HashSet;

public class VideoCategory {
    private long id;
    private String name;
    private Set videos = new HashSet();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set getVideos() {
        return videos;
    }
}
