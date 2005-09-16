package limma.plugins.music;

import limma.plugins.music.FlacPlayer;
import limma.plugins.music.MP3Player;
import limma.plugins.music.SoundPlayer;

import java.io.File;

public class SoundPlayerFactory {
    private MP3Player mp3Player;
    private FlacPlayer flacPlayer;

    public SoundPlayerFactory() {
        this.mp3Player = new MP3Player();
        this.flacPlayer = new FlacPlayer();
    }

    public SoundPlayer getPlayer(File file) {
        if (file.getName().toLowerCase().endsWith(".mp3")) {
            return mp3Player;
        }
        if (file.getName().toLowerCase().endsWith(".flac")) {
            return flacPlayer;
        }
        throw new IllegalArgumentException("Unsupported file: " + file.getAbsolutePath());
    }
}
