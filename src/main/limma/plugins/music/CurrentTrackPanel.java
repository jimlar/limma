package limma.plugins.music;

import limma.swing.AntialiasLabel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

public class CurrentTrackPanel extends JPanel {
    private AntialiasLabel artistLabel;
    private AntialiasLabel titleLabel;
    private AntialiasLabel albumLabel;
    private AntialiasLabel timeLabel;
    private int playedTime = 0;
    private int trackLength = 0;

    private AntialiasLabel tracksLabel;
    private AntialiasLabel coverLabel;

    public CurrentTrackPanel() {
        super(new GridBagLayout());
        setOpaque(false);

        tracksLabel = addLabel(0, 0, 2, 1, 1);
        coverLabel = addLabel(0, 1, 1, 3, 0);
        coverLabel.setPreferredSize(new Dimension(200, 200));

        titleLabel = addLabel(1, 1, 1, 1, 1);
        artistLabel = addLabel(1, 2, 1, 1, 1);
        albumLabel = addLabel(1, 3, 1, 1, 1);

        timeLabel = addLabel(0, 4, 2, 1, 1);
    }

    private AntialiasLabel addLabel(int column, int row, int gridwidth, int gridheight, int weightx) {
        AntialiasLabel value = new AntialiasLabel();
        value.setFont(Font.decode("Verdana").deriveFont((float) 40));
        value.setForeground(Color.black);
        add(value, new GridBagConstraints(column, row, gridwidth, gridheight, weightx, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        return value;
    }

    public void setCurrentTrack(MusicFile file, int trackNumber, int totalTracks) {
        timeLabel.setText("0:00/0:00");
        tracksLabel.setText(trackNumber + " of " + totalTracks);
        if (file == null) {
            titleLabel.setText("");
            artistLabel.setText("");
            albumLabel.setText("");
        } else {
            titleLabel.setText(file.getTitle());
            artistLabel.setText(file.getArtist());
            albumLabel.setText(file.getAlbum() + (file.getYear() == 0 ? "" : " (" + file.getYear() + ")"));
        }
        coverLabel.setIcon(findCoverImage(file));
    }

    private Icon findCoverImage(MusicFile file) {
        File dir = file.getFile().getParentFile();
        File[] jpegFiles = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        });
        if (jpegFiles != null && jpegFiles.length > 0) {
            return new ImageIcon(jpegFiles[0].getAbsolutePath());
        }
        return null;
    }

    private String secondsToString(long seconds) {
        String secs = String.valueOf(seconds % 60);
        if (secs.length() == 1) {
            secs = "0" + secs;
        }
        return seconds / 60 + ":" + secs;
    }

    public void setPlayedSeconds(int seconds) {
        this.playedTime = seconds;
        updateTime();
    }

    public void setTrackLengthSeconds(int seconds) {
        this.trackLength = seconds;
        updateTime();
    }

    private void updateTime() {
        timeLabel.setText(secondsToString(playedTime) + "/" + secondsToString(trackLength));
    }
}
