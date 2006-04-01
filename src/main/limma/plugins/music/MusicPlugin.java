package limma.plugins.music;

import limma.PlayerManager;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.NavigationModel;
import limma.swing.menu.SimpleNavigationNode;

public class MusicPlugin implements Plugin {
    private MusicPlayer musicPlayer;
    private NavigationModel navigationModel;
    private PlayerManager playerManager;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private MusicConfig musicConfig;
    private SimpleNavigationNode musicNode;

    public MusicPlugin(DialogManager dialogManager, PersistenceManager persistenceManager, MusicConfig musicConfig, MusicPlayer musicPlayer, NavigationModel navigationModel, PlayerManager playerManager) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.musicConfig = musicConfig;
        this.musicPlayer = musicPlayer;
        this.navigationModel = navigationModel;
        this.playerManager = playerManager;

        persistenceManager.addPersistentClass(MusicFile.class);
    }

    public void init() {
        musicNode = new SimpleNavigationNode("Music");
        navigationModel.add(musicNode);

        SimpleNavigationNode settingsNode = new SimpleNavigationNode("Settings");
        settingsNode.add(new SimpleNavigationNode("Scan for new music files") {
            public void performAction(DialogManager dialogManager) {
                MusicPlugin.this.dialogManager.executeInDialog(new ScanMusicFilesTask(MusicPlugin.this, persistenceManager, musicConfig));
            }
        });
        musicNode.add(settingsNode);

        dialogManager.executeInDialog(new InitializeMusicMenuTask(persistenceManager, musicNode, musicPlayer, this.playerManager));
    }

    void reloadFileList() {
        dialogManager.executeInDialog(new InitializeMusicMenuTask(persistenceManager, musicNode, musicPlayer, playerManager));
    }

}
