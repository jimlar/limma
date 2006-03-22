package limma.plugins.video;

import limma.Player;
import limma.PlayerManager;
import limma.UIProperties;
import limma.swing.AntialiasLabel;
import limma.utils.ExternalCommand;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;
import java.awt.Color;

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

    public void play(final Video video) {
        setPlayingLabel("Playing " + video.getTitle() + "...");
        playerManager.switchTo(this);
        ArrayList sortedFiles = new ArrayList(video.getFiles());
        Collections.sort(sortedFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                VideoFile file1 = (VideoFile) o1;
                VideoFile file2 = (VideoFile) o2;
                return file1.getPath().compareToIgnoreCase(file2.getPath());
            }
        });

        final String[] filenames = new String[video.getFiles().size()];
        for (ListIterator i = sortedFiles.listIterator(); i.hasNext();) {
            VideoFile file = (VideoFile) i.next();
            filenames[i.previousIndex()] = file.getPath();
        }
        Thread thread = new Thread() {
            public void run() {
                try {
                    getPlayer(video).execute(filenames);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
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
        if (video.getFiles().size() != 1) {
            return false;
        }

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
