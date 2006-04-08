package limma.plugins.music;

import limma.Player;

public interface MusicPlayer extends Player {

    void play(TrackContainerNode trackContainerNode);

    void play(TrackNode trackNode);

    boolean isShuffling();

    void setShuffling(boolean shuffle);
}
