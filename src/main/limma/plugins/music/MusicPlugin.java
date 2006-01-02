package limma.plugins.music;

import limma.PlayerManager;
import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.music.player.MusicPlayer;
import limma.swing.DialogManager;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.swing.navigationlist.NavigationModel;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * - Create a new player panel
 * - "Shuffle" is a global setting
 * - Move playlist to player
 * - Merge popup menu with normal menu
 * - Remove a lot of old crap
 * - Centralize keyboard and call methods on player interface instead
 */

public class MusicPlugin extends JPanel implements Plugin {
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private MusicConfig musicConfig;
    private DefaultNavigationNode musicNode;

    public MusicPlugin(DialogManager dialogManager, PersistenceManager persistenceManager, MusicConfig musicConfig, MusicPlayer musicPlayer) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.musicConfig = musicConfig;
        this.musicPlayer = musicPlayer;

        persistenceManager.addPersistentClass(MusicFile.class);
    }

    public void init(NavigationModel navigationModel, PlayerManager playerManager) {
        this.playerManager = playerManager;
        musicNode = new DefaultNavigationNode("Music");
        navigationModel.add(musicNode);

        DefaultNavigationNode settingsNode = new DefaultNavigationNode("Settings");
        settingsNode.add(new DefaultNavigationNode("Scan for new music files") {
            public void performAction() {
                dialogManager.executeInDialog(new ScanMusicFilesTask(MusicPlugin.this, persistenceManager, musicConfig));
            }
        });
        musicNode.add(settingsNode);

        dialogManager.executeInDialog(new LoadMusicTask(persistenceManager, musicNode, musicPlayer, this.playerManager));
    }

    public String getPluginName() {
        return "music";
    }

    public JComponent getPluginView() {
        return new JLabel("Deprecated");
    }

    public void pluginEntered() {
    }

    void reloadFileList() {
        dialogManager.executeInDialog(new LoadMusicTask(persistenceManager, musicNode, musicPlayer, playerManager));
    }

    public boolean keyPressed(KeyEvent e, PluginManager pluginManager) {
        return false;
    }
}
