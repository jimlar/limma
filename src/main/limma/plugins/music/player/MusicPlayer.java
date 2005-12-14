package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

public interface MusicPlayer {
    void addListener(PlayerListener playerListener);

    void play(MusicFile musicFile);

    void stopPlaying();
}
