package limma.domain.music;

import de.vdheide.mp3.MP3File;
import limma.application.music.MusicConfig;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class MusicFile implements Serializable {
    private long id;
    private String path;
    private String artist;
    private String title;
    private String album;
    private int year;
    private long lengthInSeconds;
    private String longName;
    private Date lastModified;

    public MusicFile() {
    }

    public MusicFile(MusicConfig musicConfig, String path, Date lastModified) {
        this.path = path;
        this.lastModified = lastModified;

        File file = musicConfig.getDiskFile(this);

        readID3WithJavaMP3(file);

        if (StringUtils.isBlank(title)) {
            title = file.getName();
            int i = title.lastIndexOf('.');
            if (i != -1) {
                title = title.substring(0, i);
            }
        }
        if (StringUtils.isBlank(album)) {
            album = file.getParentFile().getName();
        }
        if (StringUtils.isBlank(artist)) {
            artist = file.getParentFile().getParentFile().getName();
        }
        this.longName = getArtist() + ": " + getTitle();
    }

    private void readID3WithJavaMP3(File file) {
        try {
            MP3File mp3File = new MP3File(file.getAbsolutePath());
            artist = StringUtils.trimToEmpty(mp3File.getArtist().getTextContent());
            title = StringUtils.trimToEmpty(mp3File.getTitle().getTextContent());
            album = StringUtils.trimToEmpty(mp3File.getAlbum().getTextContent());
            try {
                year = Integer.parseInt(mp3File.getYear().getTextContent());
            } catch (NumberFormatException e) {
            }
            lengthInSeconds = mp3File.getLength();

        } catch (Exception e) {
        } catch (OutOfMemoryError e) {
        }
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public int getYear() {
        return year;
    }

    public long getLengthInSeconds() {
        return lengthInSeconds;
    }

    public boolean isFlac() {
        return path.toLowerCase().endsWith(".flac");
    }

    public boolean isMP3() {
        return path.toLowerCase().endsWith(".mp3");
    }

    public String toString() {
        return longName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicFile)) return false;

        final MusicFile musicFile = (MusicFile) o;

        if (!path.equals(musicFile.path)) return false;

        return true;
    }

    public int hashCode() {
        return path.hashCode();
    }

    public Date getLastModified() {
        return lastModified;
    }

    public String getPath() {
        return path;
    }
}
