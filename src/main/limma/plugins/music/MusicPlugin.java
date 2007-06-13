package limma.plugins.music;

import limma.PlayerManager;
import limma.domain.music.MusicRepository;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigation.MenuItem;
import limma.swing.navigation.NavigationModel;
import limma.swing.navigation.SimpleNavigationNode;

public class MusicPlugin implements Plugin {
    private MusicPlayer musicPlayer;
    private NavigationModel navigationModel;
    private PlayerManager playerManager;
    private MusicRepository musicRepository;
    private DialogManager dialogManager;
    private MusicConfig musicConfig;
    private SimpleNavigationNode musicNode;

    public MusicPlugin(DialogManager dialogManager, MusicConfig musicConfig, MusicPlayer musicPlayer, NavigationModel navigationModel, PlayerManager playerManager, MusicRepository musicRepository) {
        this.dialogManager = dialogManager;
        this.musicConfig = musicConfig;
        this.musicPlayer = musicPlayer;
        this.navigationModel = navigationModel;
        this.playerManager = playerManager;
        this.musicRepository = musicRepository;
    }

    public void init() {
        musicNode = new SimpleNavigationNode("Music");
        musicNode.add(new MenuItem("") {

            public String getTitle() {
                return musicPlayer.isShuffling() ? "Turn off shuffle" : "Turn on shuffle";
            }

            public void performAction(DialogManager dialogManager) {
                musicPlayer.setShuffling(!musicPlayer.isShuffling());

            }
        });
        navigationModel.add(musicNode);

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
