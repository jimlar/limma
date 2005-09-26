package limma.plugins.music.player;

import javazoom.jl.player.Player;
import limma.plugins.music.MusicFile;

import java.io.FileInputStream;

class MP3PlayerJob extends PlayerJob {
    private MusicPlayer musicPlayer;
    private boolean closingDown;
    private Player player;

    public MP3PlayerJob(MusicFile musicFile, MusicPlayer musicPlayer) {
        super(musicFile);
        this.musicPlayer = musicPlayer;
    }

    public void abort() {
        closingDown = true;
        player.close();
        Thread.currentThread().interrupt();
    }

    public void run() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(getMusicFile().getFile());
            player = new Player(fileInputStream);
            player.play();
            player.close();

        } catch (Exception e) {
            if (!closingDown) {
                e.printStackTrace();
            }
        } finally {
            if (closingDown) {
                musicPlayer.signalStopped(getMusicFile());
            } else {
                musicPlayer.signalCompleted(getMusicFile());
            }
        }
    }
}
