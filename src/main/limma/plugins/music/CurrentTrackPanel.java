package limma.plugins.music;

import limma.swing.AntialiasLabel;

import javax.swing.*;
import java.awt.*;

class CurrentTrackPanel extends JPanel {
    private AntialiasLabel artistLabel;
    private AntialiasLabel titleLabel;
    private AntialiasLabel albumLabel;
    private AntialiasLabel yearLabel;
    private AntialiasLabel genreLabel;
    private AntialiasLabel statusLabel;

    public CurrentTrackPanel() {
        super(new GridBagLayout());
        setBorder(BorderFactory.createEtchedBorder());
        setOpaque(false);

        artistLabel = addLabel("Artist:", 0);
        titleLabel = addLabel("Track:", 1);
        albumLabel = addLabel("Album:", 2);
        yearLabel = addLabel("Year:", 3);
        genreLabel = addLabel("Genre:", 4);
        statusLabel = addLabel("Status:", 5);
    }

    private AntialiasLabel addLabel(String labelText, int row) {
        add(new AntialiasLabel(labelText), new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));

        AntialiasLabel value = new AntialiasLabel();
        add(value, new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        return value;
    }

    public void setCurrentTrack(MusicFile file) {
        if (file == null) {
            artistLabel.setText("");
            titleLabel.setText("");
            albumLabel.setText("");
            yearLabel.setText("");
            genreLabel.setText("");
        } else {
            artistLabel.setText(file.getArtist());
            titleLabel.setText(file.getTitle());
            albumLabel.setText(file.getAlbum());
            yearLabel.setText(String.valueOf(file.getYear()));
            genreLabel.setText(file.getGenre());
        }
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
