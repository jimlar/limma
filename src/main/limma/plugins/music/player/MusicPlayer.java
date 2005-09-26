package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MusicPlayer {
    private PlayerJob job;
    private PlayerListener listener;
    private Executor executor;

    public MusicPlayer(PlayerListener listener) {
        this.listener = listener;
        executor = Executors.newFixedThreadPool(4, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    public void play(MusicFile musicFile) {
        if (musicFile.isMP3()) {
            job = new MP3PlayerJob(musicFile, this);
        } else {
            job = new FlacPlayerJob(musicFile, this);
        }
        executor.execute(job);
    }

    public void stop() {
        if (job != null) {
            job.abort();
            job = null;
        }
    }

    void signalStopped(MusicFile musicFile) {
        listener.stopped(musicFile);
    }

    void signalCompleted(MusicFile musicFile) {
        listener.completed(musicFile);
    }
}
