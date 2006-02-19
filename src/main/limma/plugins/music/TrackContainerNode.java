package limma.plugins.music;

import limma.swing.menu.SimpleMenuNode;
import limma.swing.menu.MenuNode;
import limma.PlayerManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class TrackContainerNode extends SimpleMenuNode {
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

        for (Iterator<MenuNode> i  = getChildren().iterator(); i.hasNext();) {
            MenuNode child = i.next();
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
