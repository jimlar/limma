package limma.plugins.music;

import limma.PlayerManager;
import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.MenuModel;
import limma.swing.menu.SimpleMenuNode;

public class MusicPlugin implements Plugin {
    private MusicPlayer musicPlayer;
    private MenuModel menuModel;
    private PlayerManager playerManager;
    private UIProperties uiProperties;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private MusicConfig musicConfig;
    private SimpleMenuNode musicNode;

    public MusicPlugin(DialogManager dialogManager, PersistenceManager persistenceManager, MusicConfig musicConfig, MusicPlayer musicPlayer, MenuModel menuModel, PlayerManager playerManager, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.musicConfig = musicConfig;
        this.musicPlayer = musicPlayer;
        this.menuModel = menuModel;
        this.playerManager = playerManager;
        this.uiProperties = uiProperties;

        persistenceManager.addPersistentClass(MusicFile.class);
    }

    public void init() {
        musicNode = new SimpleMenuNode("Music");
        menuModel.add(musicNode);

        SimpleMenuNode settingsNode = new SimpleMenuNode("Settings");
        settingsNode.add(new SimpleMenuNode("Scan for new music files") {
            public void performAction() {
                dialogManager.executeInDialog(new ScanMusicFilesTask(MusicPlugin.this, persistenceManager, musicConfig, uiProperties));
            }
        });
        musicNode.add(settingsNode);

        dialogManager.executeInDialog(new InitializeMusicMenuTask(persistenceManager, musicNode, musicPlayer, this.playerManager, uiProperties));
    }

    void reloadFileList() {
        dialogManager.executeInDialog(new InitializeMusicMenuTask(persistenceManager, musicNode, musicPlayer, playerManager, uiProperties));
    }

}
