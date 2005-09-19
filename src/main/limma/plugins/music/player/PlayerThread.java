package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

public abstract class PlayerThread extends Thread {
    private MusicFile musicFile;

    public PlayerThread(MusicFile musicFile) {
        this.musicFile = musicFile;
    }

    public MusicFile getMusicFile() {
        return musicFile;
    }

    public abstract void shutdown();
}
