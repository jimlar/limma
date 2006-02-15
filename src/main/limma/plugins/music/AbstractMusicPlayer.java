package limma.plugins.music;

import limma.UIProperties;

import javax.swing.*;
import java.util.List;
import java.util.Collections;

public abstract class AbstractMusicPlayer implements MusicPlayer {
    private CurrentTrackPanel currentTrackPanel;
    private List<MusicFile> playList = Collections.emptyList();

    protected AbstractMusicPlayer(UIProperties uiProperties) {
        currentTrackPanel = new CurrentTrackPanel(uiProperties);
    }

    protected abstract void startPlaying(MusicFile musicFile);

    public void play(TrackContainerNode trackContainerNode) {
        setPlayList(trackContainerNode.getAllMusicFiles());
        startPlaying(getNextFile());
    }

    public void play(TrackNode trackNode) {
        setPlayList(trackNode.getTrackContainer().getAllMusicFiles());
        startPlaying(trackNode.getMusicFile());
    }

    public JComponent getPlayerPane() {
        return getCurrentTrackPanel();
    }

    public void next() {
        MusicFile nextFile = getNextFile();
        if (nextFile != null) {
            startPlaying(nextFile);
        }
    }

    public void previous() {
        MusicFile previousFile = getPreviousFile();
        if (previousFile != null) {
            startPlaying(previousFile);
        }
    }

    private MusicFile getNextFile() {
        int index = getIndexOfPlayingFile();
        if (index == -1 && !playList.isEmpty()) {
            return playList.get(0);
        }
        if (index < playList.size() - 1) {
            return playList.get(index + 1);
        }
        return null;
    }

    protected abstract MusicFile getPlayingFile();

    private int getIndexOfPlayingFile() {
        return getPlayList().indexOf(getPlayingFile());
    }

    protected int getPlayingTrackNumber() {
        return getIndexOfPlayingFile() + 1;
    }

    private MusicFile getPreviousFile() {
        int index = getIndexOfPlayingFile();
        if (index > 0 && getPlayList().size() > index - 1) {
            return getPlayList().get(index - 1);
        }
        return null;
    }

    protected void setPlayedSeconds(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getCurrentTrackPanel().setPlayedSeconds(seconds);
            }
        });
    }

    protected void setTrackLengthSeconds(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                getCurrentTrackPanel().setTrackLengthSeconds(seconds);
            }
        });
    }

    private void setPlayList(List<MusicFile> playList) {
        this.playList = playList;
    }

    public List<MusicFile> getPlayList() {
        return playList;
    }

    public CurrentTrackPanel getCurrentTrackPanel() {
        return currentTrackPanel;
    }
}
