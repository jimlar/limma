package limma.plugins.music;

import limma.swing.navigationlist.DefaultNavigationNode;
import limma.plugins.music.player.MusicPlayer;
import limma.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class TrackContainerNode extends DefaultNavigationNode {
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public TrackContainerNode(String title, MusicPlayer musicPlayer, PlayerManager playerManager) {
        super(title);
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
    }

    public void performAction() {
        playerManager.switchTo(musicPlayer);
        musicPlayer.play(this);
    }

    public List<MusicFile> getAllMusicFiles() {
        ArrayList<MusicFile> musicFiles = new ArrayList<MusicFile>();
        for (int i = 0; i < getChildCount(); i++) {
            Object child =  getChildAt(i);
            if (child instanceof TrackNode) {
                TrackNode trackNode = (TrackNode) child;
                musicFiles.add(trackNode.getMusicFile());
            } else if (child instanceof TrackContainerNode) {
                musicFiles.addAll(((TrackContainerNode) child).getAllMusicFiles());
            }
        }
        return musicFiles;
    }
}
