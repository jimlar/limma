package limma.ui.music;

import limma.application.PlayerManager;
import limma.application.Plugin;
import limma.application.music.MusicPlayer;
import limma.domain.music.MusicRepository;
import limma.ui.browser.model.BrowserModel;
import limma.ui.browser.model.MenuItem;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;

public class MusicPlugin implements Plugin {
    private MusicPlayer musicPlayer;
    private BrowserModel browserModel;
    private PlayerManager playerManager;
    private MusicRepository musicRepository;
    private DialogManager dialogManager;
    private SimpleBrowserNode musicNode;

    public MusicPlugin(DialogManager dialogManager, MusicPlayer musicPlayer, BrowserModel browserModel, PlayerManager playerManager, MusicRepository musicRepository) {
        this.dialogManager = dialogManager;
        this.musicPlayer = musicPlayer;
        this.browserModel = browserModel;
        this.playerManager = playerManager;
        this.musicRepository = musicRepository;
    }

    public void init() {
        musicNode = new TrackContainerNode("Music", musicPlayer, playerManager);
        musicNode.add(new MenuItem("") {

            public String getTitle() {
                return musicPlayer.isShuffling() ? "Turn off shuffle" : "Turn on shuffle";
            }

            public void performAction(DialogManager dialogManager) {
                musicPlayer.setShuffling(!musicPlayer.isShuffling());

            }
        });
        browserModel.add(musicNode);

        musicNode.add(new MenuItem("Scan for new music files") {
            public void performAction(DialogManager dialogManager) {
                MusicPlugin.this.dialogManager.executeInDialog(new ScanMusicFilesTask(MusicPlugin.this, musicRepository));
            }
        });

        dialogManager.executeInDialog(new InitializeMusicMenuTask(musicNode, musicPlayer, this.playerManager, musicRepository));
    }

    void reloadFileList() {
        dialogManager.executeInDialog(new InitializeMusicMenuTask(musicNode, musicPlayer, playerManager, musicRepository));
    }

}
