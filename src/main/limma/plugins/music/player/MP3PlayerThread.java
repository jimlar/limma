package limma.plugins.music.player;

import javazoom.jl.player.Player;
import limma.plugins.music.MusicFile;

import java.io.FileInputStream;

class MP3PlayerThread extends PlayerThread {
    private PlayerListener listener;
    private boolean closingDown;

    public MP3PlayerThread(MusicFile musicFile, PlayerListener listener) {
        super(musicFile);
        this.listener = listener;
        setPriority(MIN_PRIORITY);
    }

    public void shutdown() {
        closingDown = true;
        interrupt();
    }

    public void run() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(getMusicFile().getFile());
            Player player = new Player(fileInputStream);
            while (!closingDown && !player.isComplete()) {
                player.play(1);
            }
            player.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (closingDown) {
                listener.stopped(getMusicFile());
            } else {
                listener.completed(getMusicFile());
            }
        }
    }
}
