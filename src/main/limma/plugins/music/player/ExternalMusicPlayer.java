package limma.plugins.music.player;

import limma.plugins.music.CurrentTrackPanel;
import limma.plugins.music.MusicConfig;
import limma.plugins.music.MusicFile;
import limma.utils.ExternalCommand;
import limma.utils.StreamForwarder;

import javax.swing.*;
import java.io.IOException;
import java.util.List;

public class ExternalMusicPlayer implements MusicPlayer {
    private MusicConfig musicConfig;
    private Process process;
    private StreamForwarder errorForwarder;
    private StreamForwarder outForwarder;
    private CurrentTrackPanel currentTrackPanel;

    public ExternalMusicPlayer(MusicConfig musicConfig) {
        this.musicConfig = musicConfig;
        currentTrackPanel = new CurrentTrackPanel();
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                ExternalMusicPlayer.this.stop();
            }
        });
    }

    public void play(List<MusicFile> musicFiles) {
        final MusicFile musicFile = musicFiles.get(0);
        stop();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setCurrentTrack(musicFile);
            }
        });

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
                            ExternalMusicPlayer.this.stop();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }.start();

        } catch (IOException e) {
            e.printStackTrace();
            stop();
        }
    }

    public void stop() {
        if (process != null) {
            process.destroy();
            try {
                process.waitFor();
            } catch (InterruptedException e) {
            }
        }

        if (errorForwarder != null) {
            errorForwarder.stop();
        }

        if (outForwarder != null) {
            outForwarder.stop();
        }
    }

    public JComponent getPlayerPane() {
        return currentTrackPanel;
    }

    public void next() {
    }

    public void previous() {
    }

    public void ff() {
    }

    public void rew() {
    }

    public void play() {
    }

    public void pause() {
    }
}
