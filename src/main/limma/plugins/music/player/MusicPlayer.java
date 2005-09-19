package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

public class MusicPlayer {
    private PlayerThread thread;

    public void play(MusicFile musicFile) {
        if (musicFile.isMP3()) {
            thread = new MP3PlayerThread(musicFile);
        } else {
            thread = new FlacPlayerThread(musicFile);
        }
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.shutdown();
            thread = null;
        }
    }
}
