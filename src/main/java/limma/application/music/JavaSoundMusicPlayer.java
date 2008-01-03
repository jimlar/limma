package limma.application.music;

import limma.application.Command;
import limma.domain.music.MusicFile;
import limma.ui.UIProperties;

public class JavaSoundMusicPlayer extends AbstractMusicPlayer {
    private JavaSoundPlayerThread playerThread;

    public JavaSoundMusicPlayer(UIProperties uiProperties, MusicConfig musicConfig) {
        super(uiProperties, musicConfig);
        playerThread = new JavaSoundPlayerThread(this, musicConfig);
    }

    protected void startPlayer(MusicFile musicFile) {
        playerThread.queue(musicFile);
    }

    protected MusicFile getPlayingFile() {
        return playerThread.getCurrentFile();
    }

    public boolean consume(Command command) {
        if (super.consume(command)) {
            return true;
        }
        switch (command) {
            case STOP:
                playerThread.signalStopping();
                return true;
        }
        return false;
    }
}
