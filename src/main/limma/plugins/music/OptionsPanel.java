package limma.plugins.music;

import limma.swing.AntialiasLabel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class OptionsPanel extends JPanel {
    private AntialiasLabel randomLabel;
    private AntialiasLabel lockArtistLabel;
    private AntialiasLabel lockAlbumLabel;
    private AntialiasLabel repeatTrackLabel;

    public OptionsPanel() {
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Options");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        setBorder(titledBorder);
        setOpaque(false);

        setLayout(new GridBagLayout());
        randomLabel = addLabel("1: Random", 0);
        lockArtistLabel = addLabel("2: Lock artist", 1);
        lockAlbumLabel = addLabel("3: Lock album", 2);
        repeatTrackLabel = addLabel("4: Repeat track", 3);
    }

    private AntialiasLabel addLabel(String labelText, int row) {
        AntialiasLabel label = new AntialiasLabel(labelText);
        add(label, new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        return label;
    }

    public void setRandom(boolean random) {
        randomLabel.setForeground(random ? Color.yellow : Color.white);
    }

    public void setLockArtist(boolean lockArtist) {
        lockArtistLabel.setForeground(lockArtist ? Color.yellow : Color.white);
    }

    public void setLockAlbum(boolean lockAlbum) {
        lockAlbumLabel.setForeground(lockAlbum ? Color.yellow : Color.white);
    }

    public void setRepeatTrack(boolean repeatTrack) {
        repeatTrackLabel.setForeground(repeatTrack ? Color.yellow : Color.white);
    }
}
