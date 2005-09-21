package limma.plugins.music;

import java.io.File;
import java.io.Serializable;

import de.vdheide.mp3.MP3File;

public class MusicFile extends Object implements Serializable {
    private File file;
    private String artist;
    private String title;
    private String album;
    private int year;
    private String genre;

    public MusicFile(File file) {
        this.file = file;

        readID3WithJavaMP3(file);
//        readID3WithJID3(file);

        if (title == null) {
            title = file.getName();
        }
        if (album == null) {
            album = file.getParentFile().getName();
        }
        if (artist == null) {
            artist = file.getParentFile().getParentFile().getName();
        }
    }

//    private void readID3WithJID3(File file) {
//        MP3File mp3File = new MP3File(file);
//        try {
//            if (mp3File.getID3V2Tag() != null) {
//                ID3V2Tag id3V2Tag = mp3File.getID3V2Tag();
//                artist = StringUtils.trim(id3V2Tag.getArtist());
//                title = StringUtils.trim(id3V2Tag.getTitle());
//                album = StringUtils.trim(id3V2Tag.getAlbum());
//                year = id3V2Tag.getYear();
//                genre = StringUtils.trim(id3V2Tag.getGenre());
//            } else if (mp3File.getID3V1Tag() != null) {
//                ID3V1Tag id3V1Tag = mp3File.getID3V1Tag();
//                artist = StringUtils.trim(id3V1Tag.getArtist());
//                title = StringUtils.trim(id3V1Tag.getTitle());
//                album = StringUtils.trim(id3V1Tag.getAlbum());
//                try {
//                    year = Integer.parseInt(id3V1Tag.getYear());
//                } catch (NumberFormatException e) {
//                }
//                ID3V1Tag.Genre genre = id3V1Tag.getGenre();
//                this.genre = genre != null ? StringUtils.trim(genre.toString()) : null;
//            }
//        } catch (ID3Exception e) {
//        }
//    }

    private void readID3WithJavaMP3(File file) {
        try {
            MP3File mp3File = new MP3File(file.getAbsolutePath());
            artist = mp3File.getArtist().getTextContent();
            title = mp3File.getTitle().getTextContent();
            album = mp3File.getAlbum().getTextContent();
            System.out.println("mp3File.getBitrate() = " + mp3File.getBitrate());
            System.out.println("mp3File.getLength() = " + mp3File.getLength());
            System.out.println("mp3File.getLengthInTag() = " + mp3File.getLengthInTag().getTextContent());
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

    public String getGenre() {
        return genre;
    }

    public boolean isFlac() {
        return file.getName().toLowerCase().endsWith(".flac");
    }

    public boolean isMP3() {
        return file.getName().toLowerCase().endsWith(".mp3");
    }

    public String toString() {
        return getArtist() + ": " + getTitle();
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
