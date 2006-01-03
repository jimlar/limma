package limma.plugins.music.player;

import limma.Player;
import limma.plugins.music.TrackContainerNode;
import limma.plugins.music.TrackNode;

public interface MusicPlayer extends Player {

    void play(TrackContainerNode trackContainerNode);

    void play(TrackNode trackNode);
}
