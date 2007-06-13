package limma.plugins.music;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import limma.UIProperties;
import limma.domain.music.MusicFile;
import limma.swing.AntialiasLabel;

public class CurrentTrackPanel extends JPanel {
    private AntialiasLabel artistLabel;
    private AntialiasLabel titleLabel;
    private AntialiasLabel albumLabel;
    private AntialiasLabel timeLabel;
    private int playedTime = 0;
    private int trackLength = 0;

    private AntialiasLabel tracksLabel;
    private CoverImage coverLabel;
    private UIProperties uiProperties;

    public CurrentTrackPanel(UIProperties uiProperties) {
        super(new GridBagLayout());
        this.uiProperties = uiProperties;

        coverLabel = new CoverImage();
        addRow(coverLabel);

        tracksLabel = addLabel();
        titleLabel = addLabel();
        artistLabel = addLabel();
        albumLabel = addLabel();

        timeLabel = addLabel();
    }

    private AntialiasLabel addLabel() {
        AntialiasLabel value = new AntialiasLabel(uiProperties);
        value.setFont(uiProperties.getLargeFont());
        value.setForeground(Color.black);
        addRow(value);
        return value;
    }

    private void addRow(JComponent component) {
        add(component, new GridBagConstraints(0, getComponentCount(), 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
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
        ImageIcon coverImage = findCoverImage(file);
        coverLabel.setCover(coverImage);
        coverLabel.setVisible(coverImage != null);
        repaintParent();
    }

    private void repaintParent() {
        Container parent = getParent();
        if (parent != null) {
            parent.repaint();
        }
    }

    private ImageIcon findCoverImage(MusicFile file) {
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
        repaintParent();
    }

    private static class CoverImage extends JComponent {
        private ImageIcon cover;

        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        public Dimension getMinimumSize() {
            return new Dimension(200, 200);
        }

        public Dimension getMaximumSize() {
            return new Dimension(200, 200);
        }

        protected void paintComponent(Graphics g) {
            if (cover != null) {
                g.drawImage(cover.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
            super.paintComponent(g);
        }

        public void setCover(ImageIcon cover) {
            this.cover = cover;
        }
    }
}
