package limma.plugins.music;

import limma.swing.AntialiasLabel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class CurrentTrackPanel extends JPanel {
    private AntialiasLabel artistLabel;
    private AntialiasLabel titleLabel;
    private AntialiasLabel albumLabel;
    private AntialiasLabel timeLabel;
    private int playedTime = 0;
    private int trackLength = 0;

    public CurrentTrackPanel() {
        super(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Track");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        setBorder(titledBorder);
        setOpaque(false);

        titleLabel = addLabel("Title:", 0);
        artistLabel = addLabel("Artist:", 1);
        albumLabel = addLabel("Album:", 2);
        timeLabel = addLabel("Time:", 3);
    }

    private AntialiasLabel addLabel(String labelText, int row) {
        add(new AntialiasLabel(labelText), new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

        AntialiasLabel value = new AntialiasLabel();
        add(value, new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        return value;
    }

    public void setCurrentTrack(MusicFile file) {
        timeLabel.setText("");
        if (file == null) {
            titleLabel.setText("");
            artistLabel.setText("");
            albumLabel.setText("");
        } else {
            titleLabel.setText(file.getTitle());
            artistLabel.setText(file.getArtist());
            albumLabel.setText(file.getAlbum() + (file.getYear() == 0 ? "" : " (" + file.getYear() + ")"));
        }
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
