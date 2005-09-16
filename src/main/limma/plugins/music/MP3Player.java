package limma.plugins.music;

import javazoom.jl.player.Player;

import java.io.File;
import java.io.FileInputStream;

public class MP3Player implements SoundPlayer {
    private PlayerThread thread;

    public void play(File file) {
        if (thread != null) {
            thread.shutdown();
        }

        thread = new PlayerThread(file);
        thread.start();
    }

    private static class PlayerThread extends Thread {
        private File soundFile;
        private boolean closingDown;

        public PlayerThread(File soundFile) {
            this.soundFile = soundFile;
        }

        public void shutdown() {
            closingDown = true;
            interrupt();
        }

        public void run() {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(soundFile);
                Player player = new Player(fileInputStream);
                while (!closingDown && !player.isComplete()) {
                    player.play(1);
                }
                player.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
