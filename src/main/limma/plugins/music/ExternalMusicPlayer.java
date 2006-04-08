package limma.plugins.music;

import limma.UIProperties;

import java.io.IOException;

public class ExternalMusicPlayer extends AbstractMusicPlayer {
    private MusicConfig musicConfig;
    private MPlayerThread mPlayerThread;

    public ExternalMusicPlayer(MusicConfig musicConfig, UIProperties uiProperties) {
        super(uiProperties);
        this.musicConfig = musicConfig;

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (mPlayerThread != null) {
                    mPlayerThread.kill();
                }
            }
        });
    }

    protected void startPlayer(MusicFile musicFile) {
        mPlayerThread = new MPlayerThread(musicConfig, musicFile, this);
        mPlayerThread.start();
    }

    public void stop() {
        if (mPlayerThread != null) {
            mPlayerThread.quit();
        }
    }

    protected MusicFile getPlayingFile() {
        if (mPlayerThread != null) {
            return mPlayerThread.getMusicFile();
        }
        return null;
    }


    public void ff() {
        mplayerCommand("seek 10 0");
    }

    public void rew() {
        mplayerCommand("seek -10 0");
    }

    public void pause() {
        mplayerCommand("pause");
    }

    private void mplayerCommand(String command) {
        if (mPlayerThread != null) {
            try {
                mPlayerThread.input(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
