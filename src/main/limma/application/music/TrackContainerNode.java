package limma.application.music;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import limma.PlayerManager;
import limma.domain.music.MusicFile;
import limma.ui.browser.NavigationNode;
import limma.ui.browser.SimpleNavigationNode;
import limma.ui.dialogs.DialogManager;

public class TrackContainerNode extends SimpleNavigationNode {
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public TrackContainerNode(String title, MusicPlayer musicPlayer, PlayerManager playerManager) {
        super(title);
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
    }

    public void performAction(DialogManager dialogManager) {
        playerManager.switchTo(musicPlayer);
        musicPlayer.play(this);
    }

    public List<MusicFile> getAllMusicFiles() {
        ArrayList<MusicFile> musicFiles = new ArrayList<MusicFile>();

        for (Iterator<NavigationNode> i = getChildren().iterator(); i.hasNext();) {
            NavigationNode child = i.next();
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
