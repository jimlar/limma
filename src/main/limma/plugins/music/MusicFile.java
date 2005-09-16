package limma.plugins.music;

import org.blinkenlights.jid3.MP3File;
import org.blinkenlights.jid3.ID3Exception;
import org.blinkenlights.jid3.v1.ID3V1Tag;
import org.blinkenlights.jid3.v2.ID3V2Tag;
import org.apache.commons.lang.StringUtils;

import java.io.File;

public class MusicFile extends Object {
    private File file;
    private String artist;
    private String title;
    private String album;
    private int year;
    private String genre;

    public MusicFile(File file) {
        this.file = file;
        MP3File mp3File = new MP3File(file);
        title = file.getName();

        try {
            if (mp3File.getID3V2Tag() != null) {
                ID3V2Tag id3V2Tag = mp3File.getID3V2Tag();
                artist = StringUtils.trim(id3V2Tag.getArtist());
                title = StringUtils.trim(id3V2Tag.getTitle());
                album = StringUtils.trim(id3V2Tag.getAlbum());
                year = id3V2Tag.getYear();
                genre = StringUtils.trim(id3V2Tag.getGenre());
            } else if (mp3File.getID3V1Tag() != null) {
                ID3V1Tag id3V1Tag = mp3File.getID3V1Tag();
                artist = StringUtils.trim(id3V1Tag.getArtist());
                title = StringUtils.trim(id3V1Tag.getTitle());
                album = StringUtils.trim(id3V1Tag.getAlbum());
                try {
                    year = Integer.parseInt(id3V1Tag.getYear());
                } catch (NumberFormatException e) {
                }
                ID3V1Tag.Genre genre = id3V1Tag.getGenre();
                this.genre = genre != null ? StringUtils.trim(genre.toString()) : null;
            }
        } catch (ID3Exception e) {
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

    public String getGenre() {
        return genre;
    }
}
