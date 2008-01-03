package limma.application.music;

import limma.application.Command;
import limma.domain.music.MusicFile;
import limma.ui.UIProperties;
import limma.ui.music.CurrentTrackPanel;
import limma.ui.music.TrackContainerNode;
import limma.ui.music.TrackNode;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class AbstractMusicPlayer implements MusicPlayer {
    private Random random = new Random();
    private List<MusicFile> playList = new ArrayList<MusicFile>();
    private List<MusicFile> history = new ArrayList<MusicFile>();
    private boolean shuffle = true;
    private CurrentTrackPanel currentTrackPanel;

    protected AbstractMusicPlayer(UIProperties uiProperties, MusicConfig musicConfig) {
        currentTrackPanel = new CurrentTrackPanel(uiProperties, musicConfig);
    }

    private void startPlaying(final MusicFile musicFile) {
        consume(Command.STOP);
        if (musicFile != null) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    currentTrackPanel.setCurrentTrack(musicFile, getPlayList().indexOf(musicFile) + 1, getPlayList().size());
                }

            });

            startPlayer(musicFile);
        }
    }

    protected abstract void startPlayer(MusicFile musicFile);

    public void play(TrackContainerNode trackContainerNode) {
        history.clear();
        setPlayList(trackContainerNode.getAllMusicFiles());
        if (isShuffling()) {
            startPlaying(getNextFile());
        } else {
            startPlaying(getPlayList().get(0));
        }
    }

    public void play(TrackNode trackNode) {
        history.clear();
        List<MusicFile> allMusicFiles = trackNode.getTrackContainer().getAllMusicFiles();
        setPlayList(allMusicFiles);
        if (!isShuffling()) {
            history.addAll(allMusicFiles.subList(0, allMusicFiles.indexOf(trackNode.getMusicFile())));
        }
        startPlaying(trackNode.getMusicFile());
    }

    public boolean isShuffling() {
        return shuffle;
    }

    public void setShuffling(boolean shuffle) {
        this.shuffle = shuffle;
    }

    public JComponent getPlayerPane() {
        return currentTrackPanel;
    }


    public boolean consume(Command command) {
        switch (command) {
            case NEXT:
                MusicFile nextFile = getNextFile();
                if (nextFile != null) {
                    history.add(getPlayingFile());
                    startPlaying(nextFile);
                }
                return true;

            case PREVIOUS:
                MusicFile previousFile = getPreviousFile();
                if (previousFile != null) {
                    history.remove(previousFile);
                    startPlaying(previousFile);
                }
                return true;
        }
        return false;
    }

    private MusicFile getNextFile() {
        if (isShuffling()) {
            ArrayList<MusicFile> candidates = new ArrayList<MusicFile>(playList);
            candidates.removeAll(history);
            candidates.remove(getPlayingFile());
            return candidates.get(random.nextInt(candidates.size()));

        } else {
            int index = getPlayList().indexOf(getPlayingFile());
            if (index == -1 && !playList.isEmpty()) {
                return playList.get(0);
            }
            if (index < playList.size() - 1) {
                return playList.get(index + 1);
            }
        }

        return null;
    }

    protected abstract MusicFile getPlayingFile();

    private MusicFile getPreviousFile() {
        if (history.isEmpty()) {
            return null;
        }
        return history.get(history.size() - 1);
    }

    protected void setPlayedSeconds(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setPlayedSeconds(seconds);
            }
        });
    }

    protected void setTrackLengthSeconds(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setTrackLengthSeconds(seconds);
            }
        });
    }

    private void setPlayList(List<MusicFile> playList) {
        this.playList = playList;
    }

    private List<MusicFile> getPlayList() {
        return playList;
    }

}
