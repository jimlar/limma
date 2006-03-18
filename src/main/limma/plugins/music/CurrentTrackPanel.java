package limma.plugins.music;

import limma.UIProperties;
import limma.swing.AntialiasLabel;

import javax.swing.*;
import javax.swing.border.LineBorder;
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
    private UIProperties uiProperties;

    public CurrentTrackPanel(UIProperties uiProperties) {
        super(new GridBagLayout());
        this.uiProperties = uiProperties;

        setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.black, 1), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        setBackground(new Color(255, 255, 255, 128));
        setOpaque(true);

        coverLabel = addLabel(0);
        coverLabel.setPreferredSize(new Dimension(200, 200));

        tracksLabel = addLabel(1);
        titleLabel = addLabel(2);
        artistLabel = addLabel(3);
        albumLabel = addLabel(4);

        timeLabel = addLabel(5);
    }

    private AntialiasLabel addLabel(int row) {
        AntialiasLabel value = new AntialiasLabel(uiProperties);
        value.setFont(uiProperties.getLargeFont());
        value.setForeground(Color.black);
        add(value, new GridBagConstraints(0, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
        Icon coverImage = findCoverImage(file);
        coverLabel.setIcon(coverImage);
        coverLabel.setVisible(coverImage != null);
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
