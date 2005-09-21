package limma.plugins.music;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import limma.swing.AntialiasLabel;

class CurrentTrackPanel extends JPanel {
    private AntialiasLabel artistLabel;
    private AntialiasLabel titleLabel;
    private AntialiasLabel albumLabel;
    private AntialiasLabel yearLabel;
    private AntialiasLabel genreLabel;
    private AntialiasLabel statusLabel;
    private AntialiasLabel playModeLabel;
    private AntialiasLabel timeLabel;
    private long startTimeMillis = 0;
    private MusicFile currentTrack;
    private AntialiasLabel bitRateLabel;

    public CurrentTrackPanel() {
        super(new GridBagLayout());
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Current track");
        titledBorder.setTitleFont(Font.decode("SansSerif").deriveFont(Font.BOLD).deriveFont((float) 30));
        titledBorder.setTitleColor(Color.white);
        setBorder(titledBorder);
        setOpaque(false);

        artistLabel = addLabel("Artist:", 0);
        titleLabel = addLabel("Track:", 1);
        albumLabel = addLabel("Album:", 2);
        yearLabel = addLabel("Year:", 3);
        genreLabel = addLabel("Genre:", 4);
        statusLabel = addLabel("Status:", 5);

        playModeLabel = new AntialiasLabel();
        add(playModeLabel, new GridBagConstraints(2, 0, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        timeLabel = new AntialiasLabel();
        add(timeLabel, new GridBagConstraints(2, 1, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));
        bitRateLabel = new AntialiasLabel();
        add(bitRateLabel, new GridBagConstraints(2, 2, 1, 1, 0, 0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 10), 0, 0));

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        SwingUtilities.invokeAndWait(new Runnable() {
                            public void run() {
                                String text = "";
                                if (currentTrack != null) {
                                    String playtime = secondsToString((System.currentTimeMillis() - startTimeMillis) / 1000);
                                    String totalTime = secondsToString(currentTrack.getLenghtInSeconds());
                                    text = playtime + " / " + totalTime;
                                }
                                timeLabel.setText(text);
                            }
                        });
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private AntialiasLabel addLabel(String labelText, int row) {
        add(new AntialiasLabel(labelText), new GridBagConstraints(0, row, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));

        AntialiasLabel value = new AntialiasLabel();
        add(value, new GridBagConstraints(1, row, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
        return value;
    }

    public void setCurrentTrack(MusicFile file) {
        this.currentTrack = file;
        if (file == null) {
            artistLabel.setText("");
            titleLabel.setText("");
            albumLabel.setText("");
            yearLabel.setText("");
            genreLabel.setText("");
            timeLabel.setText("");
            bitRateLabel.setText("");
        } else {
            startTimeMillis = System.currentTimeMillis();
            artistLabel.setText(file.getArtist());
            titleLabel.setText(file.getTitle());
            albumLabel.setText(file.getAlbum());
            yearLabel.setText(file.getYear() == 0 ? "" : String.valueOf(file.getYear()));
            genreLabel.setText(file.getGenre());
            bitRateLabel.setText(file.getBitRate() + " kbps");
        }
    }

    private String secondsToString(long seconds) {
        String secs = String.valueOf(seconds % 60);
        if (secs.length() == 1) {
            secs = "0" + secs;
        }
        return seconds / 60 + ":" + secs;
    }

    public void setPlayStrategy(PlayStrategy strategy) {
        playModeLabel.setText(strategy.getName());
    }

    public void setStatus(String message) {
        statusLabel.setText(message);
    }
}
