package limma.ui.music;

import limma.application.PlayerManager;
import limma.application.music.MusicPlayer;
import limma.domain.music.MusicFile;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;

import java.io.File;

public class TrackNode extends SimpleBrowserNode {
    private MusicFile musicFile;
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public TrackNode(String title, MusicFile musicFile, MusicPlayer musicPlayer, PlayerManager playerManager) {
        super(title);
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
        this.musicFile = musicFile;
    }

    public void performAction(DialogManager dialogManager) {
        playerManager.switchTo(musicPlayer);
        musicPlayer.play(this);
    }

    public MusicFile getMusicFile() {
        return musicFile;
    }

    public TrackContainerNode getTrackContainer() {
        return (TrackContainerNode) getParent();
    }

    public int compareTo(Object o) {
        if (o instanceof TrackNode) {
            return new File(getMusicFile().getPath()).getName().compareToIgnoreCase(new File(((TrackNode) o).getMusicFile().getPath()).getName());
        }

        return getTitle().compareToIgnoreCase(((BrowserModelNode) o).getTitle());
    }
}
