package limma.plugins.music;

import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.music.player.MusicPlayer;
import limma.plugins.music.player.PlayerListener;
import limma.swing.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MusicPlugin extends JPanel implements Plugin {
    private SimpleListModel musicListModel;
    private AntialiasList musicList;
    private MusicPlayer musicPlayer;
    private boolean hasBeenEntered;
    private CurrentTrackPanel currentTrackPanel;
    private MusicFile playedFile;
    private PlayStrategy selectedPlayStrategy;
    private RandomPlayStrategy randomPlayStrategy;
    private LinearPlayStrategy linearPlayStrategy;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private OptionsPanel optionsPanel;
    private boolean lockAlbum;
    private boolean lockArtist;
    private boolean repeatTrack;
    private MenuDialog menuDialog;

    public MusicPlugin(DialogManager dialogManager, PersistenceManager persistenceManager) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        persistenceManager.addPersistentClass(MusicFile.class);
        setOpaque(false);
        setLayout(new GridBagLayout());

        currentTrackPanel = new CurrentTrackPanel();

        musicListModel = new SimpleListModel();
        musicList = new AntialiasList(musicListModel);
        musicList.setCellRenderer(new MusicListCellRenderer());
        JScrollPane playlistScrollPane = new JScrollPane(musicList);


        TitledBorder titledBorder = BorderFactory.createTitledBorder("Playlist");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);

        playlistScrollPane.setBorder(titledBorder);
        playlistScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        playlistScrollPane.setOpaque(false);
        playlistScrollPane.getViewport().setOpaque(false);

        optionsPanel = new OptionsPanel();
        add(currentTrackPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
        add(optionsPanel, new GridBagConstraints(1, 0, 1, 1, 0, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
        add(playlistScrollPane, new GridBagConstraints(0, 1, 2, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


        musicPlayer = new MusicPlayer(new PlayerListener() {
            public void stopped(MusicFile musicFile) {
            }

            public void completed(MusicFile musicFile) {
                playNextTrack(musicFile);
            }
        });
        randomPlayStrategy = new RandomPlayStrategy(musicListModel);
        linearPlayStrategy = new LinearPlayStrategy(musicListModel);
        selectedPlayStrategy = randomPlayStrategy;
        optionsPanel.setRandom(true);

        menuDialog = new MenuDialog(dialogManager);
        menuDialog.addItem(new LimmaMenuItem("Scan for new music files") {
            public void execute() {
                scanFiles();
            }
        });
    }

    private void playNextTrack() {
        playNextTrack(playedFile);
    }

    private void playNextTrack(final MusicFile lastTrack) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                boolean jumpToTrack = lastTrack == null || (musicList.getSelectedValue() != null && musicList.getSelectedValue().equals(lastTrack));

                MusicFile nextFileToPlay;
                if (repeatTrack) {
                    nextFileToPlay = lastTrack;
                } else {
                    nextFileToPlay = selectedPlayStrategy.getNextFileToPlay(lastTrack, lockArtist, lockAlbum);
                }
                if (nextFileToPlay == null) {
                    stop();
                } else {
                    play(nextFileToPlay);
                }
                if (jumpToTrack) {
                    musicList.setSelectedValue(nextFileToPlay, true);
                }
            }
        });
    }

    public String getPluginName() {
        return "music";
    }

    public JComponent getPluginView() {
        return this;
    }

    public void pluginEntered() {
        if (!hasBeenEntered) {
            reloadFileList();
            hasBeenEntered = true;
        }
    }

    private void scanFiles() {
        dialogManager.executeInDialog(new ScanMusicFilesTask(this, persistenceManager));
    }

    void reloadFileList() {
        dialogManager.executeInDialog(new LoadPlayListTask(this, persistenceManager));
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
            case KeyEvent.VK_ENTER:
                play((MusicFile) musicList.getSelectedValue());
                break;
            case KeyEvent.VK_S:
                stop();
                break;
            case KeyEvent.VK_M:
                menuDialog.open();
                break;
            case KeyEvent.VK_1:
                toggleRandom();
                break;
            case KeyEvent.VK_2:
                toggleLockArtist();
                break;
            case KeyEvent.VK_3:
                toggelLockAlbum();
                break;
            case KeyEvent.VK_4:
                toggelRepeatTrack();
                break;
            case KeyEvent.VK_N:
                playNextTrack();
                break;
            default:
                musicList.processKeyEvent(e);
        }
    }

    private void toggelRepeatTrack() {
        repeatTrack = !repeatTrack;
        optionsPanel.setRepeatTrack(repeatTrack);
    }

    private void toggelLockAlbum() {
        lockAlbum = !lockAlbum;
        if (lockAlbum) {
            lockArtist = true;
        }
        optionsPanel.setLockArtist(lockArtist);
        optionsPanel.setLockAlbum(lockAlbum);
    }

    private void toggleLockArtist() {
        lockArtist = !lockArtist;
        if (!lockArtist) {
            lockAlbum = false;
        }
        optionsPanel.setLockArtist(lockArtist);
        optionsPanel.setLockAlbum(lockAlbum);
    }

    private void toggleRandom() {
        if (selectedPlayStrategy instanceof LinearPlayStrategy) {
            selectedPlayStrategy = randomPlayStrategy;
            optionsPanel.setRandom(true);
        } else {
            selectedPlayStrategy = linearPlayStrategy;
            optionsPanel.setRandom(false);
        }
    }

    public void stop() {
        musicPlayer.stop();
        currentTrackPanel.setCurrentTrack(null);
        MusicFile lastPlayed = playedFile;
        playedFile = null;
        musicListModel.fireChanged(lastPlayed);
        musicListModel.fireChanged(playedFile);
    }

    public void play(MusicFile file) {
        musicPlayer.stop();
        musicPlayer.play(file);
        currentTrackPanel.setCurrentTrack(file);
        MusicFile lastPlayed = playedFile;
        playedFile = file;
        musicListModel.fireChanged(lastPlayed);
        musicListModel.fireChanged(playedFile);
    }

    private class MusicListCellRenderer extends AntialiasCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component listCellRendererComponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value.equals(playedFile)) {
                listCellRendererComponent.setForeground(Color.yellow);
            }
            return listCellRendererComponent;
        }
    }

    private static class LoadPlayListTask implements Task {
        private MusicPlugin musicPlugin;
        private PersistenceManager persistenceManager;

        public LoadPlayListTask(MusicPlugin musicPlugin, PersistenceManager persistenceManager) {
            this.musicPlugin = musicPlugin;
            this.persistenceManager = persistenceManager;
        }

        public JComponent createComponent() {
            return new AntialiasLabel("Loading file list...");
        }

        public void run() {
            final java.util.List musicFiles = persistenceManager.query("all_musicfiles");
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    musicPlugin.musicListModel.setObjects(musicFiles);
                    musicPlugin.musicList.setSelectedIndex(0);
                }
            });
        }
    }
}
