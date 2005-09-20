package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

public class MusicPlayer {
    private PlayerThread thread;
    private PlayerListener listener;

    public MusicPlayer(PlayerListener listener) {
        this.listener = listener;
    }

    public void play(MusicFile musicFile) {
        if (musicFile.isMP3()) {
            thread = new MP3PlayerThread(musicFile, this);
        } else {
            thread = new FlacPlayerThread(musicFile, this);
        }
        thread.start();
    }

    public void stop() {
        if (thread != null) {
            thread.shutdown();
            thread = null;
        }
    }

    void signalStopped(MusicFile musicFile) {
        listener.stopped(musicFile);
    }

    void signalCompleted(MusicFile musicFile) {
        listener.completed(musicFile);
    }
}
