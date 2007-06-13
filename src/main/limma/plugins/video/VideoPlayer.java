package limma.plugins.video;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import limma.Player;
import limma.PlayerManager;
import limma.UIProperties;
import limma.domain.video.Video;
import limma.domain.video.VideoFile;
import limma.swing.AntialiasLabel;
import limma.utils.ExternalCommand;

public class VideoPlayer implements Player {
    private static final long DVD_SIZE_THRESHOLD = 2L * 1024 * 1024 * 1024;
    private VideoConfig videoConfig;
    private PlayerManager playerManager;
    private AntialiasLabel playingLabel;

    public VideoPlayer(VideoConfig videoConfig, PlayerManager playerManager, UIProperties uiProperties) {
        this.videoConfig = videoConfig;
        this.playerManager = playerManager;
        this.playingLabel = new AntialiasLabel("Stopped", uiProperties);
        playingLabel.setForeground(Color.black);
    }

    public JComponent getPlayerPane() {
        return playingLabel;
    }

    public void next() {
    }

    public void previous() {
    }

    public void ff() {
    }

    public void rew() {
    }

    public void pause() {
    }

    public void stop() {
    }


    public void play(VideoFile file) {
        playVideoFiles(file.getVideo(), Collections.singletonList(file));
    }

    public void play(final Video video) {
        ArrayList sortedFiles = new ArrayList(video.getFiles());
        Collections.sort(sortedFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                VideoFile file1 = (VideoFile) o1;
                VideoFile file2 = (VideoFile) o2;
                return file1.getPath().compareToIgnoreCase(file2.getPath());
            }
        });

        playVideoFiles(video, sortedFiles);
    }

    private void playVideoFiles(final Video video, List filesToPlay) {
        setPlayingLabel("Playing " + video.getTitle() + "...");
        playerManager.switchTo(this);
        final String[] filenames = new String[filesToPlay.size()];
        for (ListIterator i = filesToPlay.listIterator(); i.hasNext();) {
            VideoFile file = (VideoFile) i.next();
            filenames[i.previousIndex()] = file.getPath();
        }
        Thread thread = new Thread() {
            public void run() {
                try {
                    ExternalCommand player = getPlayer(video);
                    if (isDvd(video)) {
                        for (int i = 0; i < filenames.length; i++) {
                            String filename = filenames[i];
                            player.execute(filename);
                        }
                    } else {
                        player.execute(filenames);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    setPlayingLabel(video.getTitle() + " (stopped)");
                }
            }
        };
        thread.start();
    }

    private void setPlayingLabel(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                playingLabel.setText(text);
            }
        });
    }

    private ExternalCommand getPlayer(Video video) {
        if (isDvd(video)) {
            return videoConfig.getDvdPlayerCommand();

        } else {
            return videoConfig.getDefaultPlayerCommand();
        }
    }

    private boolean isDvd(Video video) {
        VideoFile file = (VideoFile) video.getFiles().iterator().next();

        /* Its a dvd on disk directory */
        if (new File(file.getFile(), "VIDEO_TS.IFO").isFile()) {
            return true;
        }

        /* 4GB files are considered to be dvds */
        if (file.getFile().isFile() && file.getFile().length() > DVD_SIZE_THRESHOLD) {
            return true;
        }

        return false;
    }
}
