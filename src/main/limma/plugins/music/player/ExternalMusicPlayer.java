package limma.plugins.music.player;

import limma.plugins.music.MusicConfig;
import limma.plugins.music.MusicFile;
import limma.utils.ExternalCommand;
import limma.utils.StreamForwarder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExternalMusicPlayer implements MusicPlayer {
    private MusicConfig musicConfig;
    private Process process;
    private StreamForwarder errorForwarder;
    private StreamForwarder outForwarder;
    private List<PlayerListener> listeners = new ArrayList<PlayerListener>();
    private MusicFile musicFile;

    public ExternalMusicPlayer(MusicConfig musicConfig) {
        this.musicConfig = musicConfig;
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                stopPlaying();
            }
        });
    }

    public void addListener(PlayerListener playerListener) {
        listeners.add(playerListener);
    }

    public void play(final MusicFile musicFile) {
        this.musicFile = musicFile;
        stopPlaying();

        ExternalCommand playerCommand = musicConfig.getExternalPlayerCommand();
        try {
            process = Runtime.getRuntime().exec(playerCommand.getCommandLine(musicFile.getFile().getAbsolutePath()));
            errorForwarder = new StreamForwarder(process.getErrorStream(), System.err);
            outForwarder = new StreamForwarder(process.getInputStream(), System.out);

            new Thread() {
                public void run() {
                    try {
                        int exitCode = process.waitFor();
                        if (exitCode == 0) {
                            process = null;
                            for (Iterator<PlayerListener> i = listeners.iterator(); i.hasNext();) {
                                PlayerListener listener = i.next();
                                listener.completed(musicFile);
                            }
                            stopPlaying();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }.start();

        } catch (IOException e) {
            e.printStackTrace();
            stopPlaying();
        }
    }

    public void stopPlaying() {
        if (process != null) {
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
            }
            for (Iterator<PlayerListener> i = listeners.iterator(); i.hasNext();) {
                PlayerListener listener = i.next();
                listener.stopped(musicFile);
            }
        }

        if (errorForwarder != null) {
            errorForwarder.stop();
        }

        if (outForwarder != null) {
            outForwarder.stop();
        }
    }
}
