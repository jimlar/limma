package limma.application.music;

import java.io.IOException;

import limma.Command;
import limma.domain.music.MusicFile;
import limma.ui.UIProperties;

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


    public boolean consume(Command command) {
        if (super.consume(command)) {
            return true;
        }
        switch (command) {
            case STOP:
                if (mPlayerThread != null) {
                    mPlayerThread.quit();
                }
                return true;

            case FF:
                mplayerCommand("seek 10 0");
                return true;

            case REW:
                mplayerCommand("seek -10 0");
                return true;

            case PAUSE:
                mplayerCommand("pause");
                return true;
        }
        return false;
    }

    protected MusicFile getPlayingFile() {
        if (mPlayerThread != null) {
            return mPlayerThread.getMusicFile();
        }
        return null;
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
