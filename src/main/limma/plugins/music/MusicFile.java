package limma.plugins.music;

import de.vdheide.mp3.MP3File;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.Serializable;

public class MusicFile extends Object implements Serializable {
    private File file;
    private String artist;
    private String title;
    private String album;
    private int year;
    private long lenghtInSeconds;
    private String longName;

    public MusicFile(File file) {
        this.file = file;

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
            lenghtInSeconds = mp3File.getLength();

        } catch (Exception e) {
        }
    }

    public File getFile() {
        return file;
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

    public long getLenghtInSeconds() {
        return lenghtInSeconds;
    }

    public boolean isFlac() {
        return file.getName().toLowerCase().endsWith(".flac");
    }

    public boolean isMP3() {
        return file.getName().toLowerCase().endsWith(".mp3");
    }

    public String toString() {
        return longName;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MusicFile)) return false;

        final MusicFile musicFile = (MusicFile) o;

        if (!file.equals(musicFile.file)) return false;

        return true;
    }

    public int hashCode() {
        return file.hashCode();
    }

}
