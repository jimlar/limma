package limma.plugins.music;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.music.player.MusicPlayer;
import limma.plugins.music.player.PlayerListener;
import limma.swing.*;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.List;

public class MusicPlugin extends JPanel implements Plugin {
    static final File MUSIC_CACHE = new File("music.cache");
    private SimpleListModel musicListModel;
    private AntialiasList musicList;
    private MusicPlayer musicPlayer;
    private boolean hasBeenEntered;
    private CurrentTrackPanel currentTrackPanel;
    private MusicFile playedFile;
    private PlayStrategy selectedPlayStrategy;
    private RandomPlayStrategy randomPlayStrategy;
    private LinearPlayStrategy linearPlayStrategy;
    private JDesktopPane desktopPane;

    public MusicPlugin(JDesktopPane desktopPane) {
        this.desktopPane = desktopPane;
        setOpaque(false);
        setLayout(new GridBagLayout());

        currentTrackPanel = new CurrentTrackPanel();
        add(currentTrackPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 10, 0), 0, 0));

        musicListModel = new SimpleListModel();
        musicList = new AntialiasList(musicListModel);
        musicList.setCellRenderer(new MusicListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(musicList);
        add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Playlist");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        scrollPane.setBorder(titledBorder);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

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
        currentTrackPanel.setPlayStrategy(selectedPlayStrategy);
    }

    private void playNextTrack() {
        playNextTrack(playedFile);
    }

    private void playNextTrack(MusicFile lastTrack) {
        boolean jumpToNextTrack = false;
        if (musicList.getSelectedValue() != null && musicList.getSelectedValue().equals(lastTrack)) {
            jumpToNextTrack = true;
        }
        MusicFile nextFileToPlay = selectedPlayStrategy.getNextFileToPlay(lastTrack);
        if (nextFileToPlay == null) {
            stop();
        } else {
            play(nextFileToPlay);
        }
        if (jumpToNextTrack) {
            musicList.setSelectedValue(nextFileToPlay, true);
        }
    }

    public String getPluginName() {
        return "music";
    }

    public JComponent getPluginComponent() {
        return this;
    }

    public void pluginEntered() {
        if (!hasBeenEntered) {
            reloadFileList();
            hasBeenEntered = true;
        }
    }

    private void scanFiles() {
        ProcessDialog dialog = new ProcessDialog(desktopPane);
        dialog.executeInDialog(new ScanFilesJob(this));
    }

    void reloadFileList() {
        ProcessDialog dialog = new ProcessDialog(desktopPane);
        dialog.executeInDialog(new LoadListJob(this));
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
            case KeyEvent.VK_R:
                scanFiles();
                break;
            case KeyEvent.VK_M:
                cycleStrategy();
                break;
            case KeyEvent.VK_N:
                playNextTrack();
                break;
        }
        musicList.processKeyEvent(e);
    }

    private void cycleStrategy() {
        if (selectedPlayStrategy instanceof LinearPlayStrategy) {
            selectedPlayStrategy = randomPlayStrategy;
        } else {
            selectedPlayStrategy = linearPlayStrategy;
        }
        currentTrackPanel.setPlayStrategy(selectedPlayStrategy);
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

    private static class LoadListJob implements ProcessDialog.Job {
        private MusicPlugin musicPlugin;

        public LoadListJob(MusicPlugin musicPlugin) {
            this.musicPlugin = musicPlugin;
        }

        public JComponent init(ProcessDialog processDialog) {
            return new AntialiasLabel("Loading file list...");
        }

        public void run() {
            musicPlugin.musicListModel.setObjects(loadFiles());
            musicPlugin.musicList.setSelectedIndex(0);
        }

        private List loadFiles() {
            ObjectInputStream in = null;
            try {
                in = new ObjectInputStream(new FileInputStream(MUSIC_CACHE));
                return (List) in.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(in);
            }
            return Collections.EMPTY_LIST;
        }
    }

}
