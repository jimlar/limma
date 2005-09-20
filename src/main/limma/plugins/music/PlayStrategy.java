package limma.plugins.music;

public interface PlayStrategy {
    MusicFile getNextFileToPlay(MusicFile lastFile);

    String getName();
}
