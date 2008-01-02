package limma.domain.video;

import java.util.*;


public class Video {
    private String title;
    private String director;
    private String runtime;
    private int year;
    private String plot;
    private String rating;
    private int imdbNumber;
    private Set<VideoFile> files = new HashSet<VideoFile>();
    private Set<String> tags = new HashSet<String>();
    private Date created;
    private Set<String> genres = new HashSet<String>();
    private List<String> actors = new ArrayList<String>();


    public Video(String title) {
        this.title = title;
        this.created = new Date();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String toString() {
        return title;
    }

    public Set<VideoFile> getFiles() {
        return files;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getImdbNumber() {
        return imdbNumber;
    }

    public void setImdbNumber(int imdbNumber) {
        this.imdbNumber = imdbNumber;
    }

    public boolean hasImdbNumber() {
        return getImdbNumber() != 0;
    }

    public Set<String> getTags() {
        return tags;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public void add(VideoFile videoFile) {
        files.add(videoFile);
        videoFile.setVideo(this);
    }

    public void remove(VideoFile videoFile) {
        files.remove(videoFile);
        videoFile.setVideo(null);
    }


    public void setFiles(Set<VideoFile> files) {
        this.files = files;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }

    public Set<String> getGenres() {
        if (genres == null) {
            return Collections.emptySet();
        }
        return genres;
    }

    public List<String> getActors() {
        if (actors == null) {
            return Collections.emptyList();
        }
        return actors;
    }
}
