package limma.application.video;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class IMDBInfo {
    private int imdbNumber;
    private String director;
    private String title;
    private String runtime;
    private String plot;
    private String rating;
    private int year;
    private String cover;

    public int getImdbNumber() {
        return imdbNumber;
    }

    public void setImdbNumber(int imdbNumber) {
        this.imdbNumber = imdbNumber;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCover() {
        return cover;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }
}
