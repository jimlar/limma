package limma.plugins.music;

import limma.UIProperties;
import limma.domain.music.MusicFile;

public class JavaSoundMusicPlayer extends AbstractMusicPlayer {
    private JavaSoundPlayerThread playerThread;

    public JavaSoundMusicPlayer(UIProperties uiProperties) {
        super(uiProperties);
        playerThread = new JavaSoundPlayerThread(this);
    }

    protected void startPlayer(MusicFile musicFile) {
        playerThread.queue(musicFile);
    }

    protected MusicFile getPlayingFile() {
        return playerThread.getCurrentFile();
    }

    public void stop() {
        playerThread.signalStopping();
    }

    public void ff() {
    }

    public void rew() {
    }

    public void pause() {
    }

}
