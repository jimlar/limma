package limma.plugins.music;

import limma.PlayerManager;
import limma.swing.navigationlist.MenuNode;

public class TrackNode extends MenuNode {
    private MusicFile musicFile;
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public TrackNode(String title, MusicFile musicFile, MusicPlayer musicPlayer, PlayerManager playerManager) {
        super(title);
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
        this.musicFile = musicFile;
    }

    public void performAction() {
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
