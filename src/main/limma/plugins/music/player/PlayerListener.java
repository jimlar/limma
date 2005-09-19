package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

public interface PlayerListener {
    void stopped(MusicFile musicFile);

    void completed(MusicFile musicFile);
}
