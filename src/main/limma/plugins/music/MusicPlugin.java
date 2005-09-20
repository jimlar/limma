package limma.plugins.music;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.music.player.MusicPlayer;
import limma.plugins.music.player.PlayerListener;
import limma.swing.AntialiasCellRenderer;
import limma.swing.AntialiasList;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class MusicPlugin extends JPanel implements Plugin {
    private MusicListModel musicListModel;
    private AntialiasList musicList;
    private MusicPlayer musicPlayer;
    private static final File MUSIC_CACHE = new File("music.cache");
    private boolean hasBeenEntered;
    private CurrentTrackPanel currentTrackPanel;
    private MusicFile playedFile;
    private PlayStrategy selectedPlayStrategy;
    private RandomPlayStrategy randomPlayStrategy;
    private LinearPlayStrategy linearPlayStrategy;

    public MusicPlugin() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        currentTrackPanel = new CurrentTrackPanel();
        add(currentTrackPanel, new GridBagConstraints(0, 0, 1, 1, 1, 0, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 20, 0), 0, 0));

        musicListModel = new MusicListModel();
        musicList = new AntialiasList(musicListModel);
        musicList.setCellRenderer(new MusicListCellRenderer());
        JScrollPane scrollPane = new JScrollPane(musicList);
        add(scrollPane, new GridBagConstraints(0, 1, 1, 1, 1, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Playlist");
        titledBorder.setTitleFont(Font.decode("SansSerif").deriveFont(Font.BOLD).deriveFont((float) 30));
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

    private void reloadFileList() {
        new Thread() {
            public void run() {
                setStatus("Loading file list...");
                musicListModel.setMusicFiles(loadFiles());
                musicList.setSelectedIndex(0);
                setStatus("");
            }
        }.start();
    }

    private void setStatus(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setStatus(message);
            }
        });
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

    private void scanFiles() {
        new Thread() {
            public void run() {
                File musicDir = new File("/media/music");
                setStatus("Scanning for music files in " + musicDir.getAbsolutePath());
                ArrayList files = new ArrayList();
                scanAndAddFiles(musicDir, files);
                ObjectOutputStream out = null;
                try {
                    out = new ObjectOutputStream(new FileOutputStream(MUSIC_CACHE));
                    out.writeObject(files);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(out);
                    setStatus("");
                }
                reloadFileList();
            }
        }.start();
    }

    private void scanAndAddFiles(File dir, List result) {
        File[] fileArray = dir.listFiles();
        if (fileArray == null) {
            return;
        }
        ArrayList files = new ArrayList(Arrays.asList(fileArray));
        Collections.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((File) o1).getName().compareToIgnoreCase(((File) o2).getName());
            }
        });
        for (Iterator i = files.iterator(); i.hasNext();) {
            File file = (File) i.next();
            if (file.isDirectory()) {
                scanAndAddFiles(file, result);
            } else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".flac")) {
                result.add(new MusicFile(file));
            }
        }
    }

    private class MusicListCellRenderer extends AntialiasCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component listCellRendererComponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value.equals(playedFile)) {
                listCellRendererComponent.setForeground(Color.gray);
            }
            return listCellRendererComponent;
        }
    }
}
