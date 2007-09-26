package limma.application.music;

import limma.PlayerManager;
import limma.domain.music.MusicFile;
import limma.ui.browser.SimpleNavigationNode;
import limma.ui.dialogs.DialogManager;

public class TrackNode extends SimpleNavigationNode {
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
}
