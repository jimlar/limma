package limma.plugins.video;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


public class Video {
    private long id;
    private String title;
    private String director;
    private String runtime;
    private int year;
    private String plot;
    private String rating;
    private int imdbNumber;
    private Set files = new HashSet();
    private Set categories = new HashSet();


    public Video() {
    }

    public Video(String title) {
        this.title = title;
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

    public Set getFiles() {
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

    public long getId() {
        return id;
    }

    public boolean hasImdbNumber() {
        return getImdbNumber() != 0;
    }

    public Set getCategories() {
        return categories;
    }
}
