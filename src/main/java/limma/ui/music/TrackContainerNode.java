package limma.ui.music;

import limma.application.PlayerManager;
import limma.application.music.MusicPlayer;
import limma.domain.music.MusicFile;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TrackContainerNode extends SimpleBrowserNode {
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

        for (Iterator<BrowserModelNode> i = getChildren().iterator(); i.hasNext();) {
            BrowserModelNode child = i.next();
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
