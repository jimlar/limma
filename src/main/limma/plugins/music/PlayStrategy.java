package limma.plugins.music;

public interface PlayStrategy {
    MusicFile getNextFileToPlay(MusicFile lastFile, boolean lockArtist, boolean lockAlbum);
}
