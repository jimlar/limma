package limma.plugins.music.player;

import limma.plugins.music.MusicFile;
import limma.Player;

import java.util.List;

public interface MusicPlayer extends Player {

    void play(List<MusicFile> musicFiles);

}
