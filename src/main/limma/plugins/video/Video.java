package limma.plugins.video;

import java.util.HashSet;
import java.util.Set;


public class Video {
    private long id;
    private String name;
    private boolean dvd;
    private Set files = new HashSet();
    private String director;
    private String length;
    private int year;
    private String plot;
    private String rating;
    private int imdbNumber;

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

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
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
}
