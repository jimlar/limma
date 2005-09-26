package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

public abstract class PlayerJob implements Runnable {
    private MusicFile musicFile;

    public PlayerJob(MusicFile musicFile) {
        this.musicFile = musicFile;
    }

    public MusicFile getMusicFile() {
        return musicFile;
    }

    public abstract void abort();
}
