package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

import java.util.List;

public interface MusicPlayer {
    void addListener(PlayerListener playerListener);

    void play(MusicFile musicFile);

    void play(List<MusicFile> musicFiles);

    void stopPlaying();

}
