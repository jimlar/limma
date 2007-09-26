package limma.application.music;

import limma.ui.Player;

public interface MusicPlayer extends Player {

    void play(TrackContainerNode trackContainerNode);

    void play(TrackNode trackNode);

    boolean isShuffling();

    void setShuffling(boolean shuffle);
}
