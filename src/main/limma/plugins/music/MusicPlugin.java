package limma.plugins.music;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasList;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;
import java.util.List;

public class MusicPlugin extends JPanel implements Plugin {
    private DefaultListModel fileListModel;
    private AntialiasList antialiasList;
    private FlacPlayer flacPlayer;
    private MP3Player mp3Player;
    private JLabel artistLabel;
    private JLabel titleLabel;
    private JLabel albumLabel;
    private JLabel yearLabel;
    private JLabel genreLabel;
    private JLabel statusLabel;
    private MusicFile playingFile;
    private static final File MUSIC_CACHE = new File("music.cache");
    private boolean hasBeenActivated;

    public MusicPlugin() {
        setOpaque(false);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileListModel = new DefaultListModel();
        antialiasList = new AntialiasList(fileListModel);
        JScrollPane scrollPane = new JScrollPane(antialiasList);
        add(scrollPane, new GridBagConstraints(0, 0, 1, 1, 1, 0.6, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        scrollPane.setBorder(BorderFactory.createEtchedBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel currentTrackPanel = new JPanel(new GridBagLayout());
        add(currentTrackPanel, new GridBagConstraints(0, 1, 1, 1, 0.8, 0.2, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 0));
        currentTrackPanel.setBorder(BorderFactory.createEtchedBorder());
        currentTrackPanel.setOpaque(false);

        artistLabel = new JLabel();
        titleLabel = new JLabel();
        albumLabel = new JLabel();
        yearLabel = new JLabel();
        genreLabel = new JLabel();
        statusLabel = new JLabel();
        artistLabel.setForeground(Color.white);
        titleLabel.setForeground(Color.white);
        albumLabel.setForeground(Color.white);
        yearLabel.setForeground(Color.white);
        genreLabel.setForeground(Color.white);
        statusLabel.setForeground(Color.white);
        currentTrackPanel.add(new JLabel("Artist:"), getConstraint(0, true));
        currentTrackPanel.add(artistLabel, getConstraint(0, false));
        currentTrackPanel.add(new JLabel("Title:"), getConstraint(1, true));
        currentTrackPanel.add(titleLabel, getConstraint(1, false));
        currentTrackPanel.add(new JLabel("Album:"), getConstraint(2, true));
        currentTrackPanel.add(albumLabel, getConstraint(2, false));
        currentTrackPanel.add(new JLabel("Year:"), getConstraint(3, true));
        currentTrackPanel.add(yearLabel, getConstraint(3, false));
        currentTrackPanel.add(new JLabel("Genre:"), getConstraint(4, true));
        currentTrackPanel.add(genreLabel, getConstraint(4, false));
        currentTrackPanel.add(new JLabel("-----"), getConstraint(5, true));
        currentTrackPanel.add(new JLabel("Status:"), getConstraint(6, true));
        currentTrackPanel.add(statusLabel, getConstraint(6, false));

        JPanel shortKeysPanel = new JPanel(new GridBagLayout());
        add(shortKeysPanel, new GridBagConstraints(0, 2, 1, 1, 0.2, 0.2, GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL, new Insets(20, 0, 0, 0), 0, 0));
        shortKeysPanel.setBorder(BorderFactory.createEtchedBorder());
        shortKeysPanel.setOpaque(false);
        shortKeysPanel.add(new JLabel("P - play"), getConstraint(0, false));
        shortKeysPanel.add(new JLabel("S - stop"), getConstraint(1, false));
        shortKeysPanel.add(new JLabel("R - rescan music directory:"), getConstraint(2, false));

        flacPlayer = new FlacPlayer();
        mp3Player = new MP3Player();
    }

    private GridBagConstraints getConstraint(int row, boolean isLabel) {
        return new GridBagConstraints(isLabel ? 0 : 1,
                row,
                1,
                1,
                isLabel ? 0 : 1,
                0,
                GridBagConstraints.NORTHWEST,
                isLabel ? GridBagConstraints.NONE : GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 0, 0),
                0,
                0);
    }

    public String getPluginName() {
        return "music";
    }

    public JComponent getPluginComponent() {
        return this;
    }

    public void activatePlugin() {
        if (!hasBeenActivated) {
            reloadFileList();
            hasBeenActivated = true;
        }
    }

    private void reloadFileList() {
        fileListModel.clear();
        setStatus("Loading file list...");

        new Thread() {
            public void run() {
                for (Iterator i = loadFiles().iterator(); i.hasNext();) {
                    MusicFile file = (MusicFile) i.next();
                    fileListModel.addElement(file);
                }

                antialiasList.setSelectedIndex(0);
                setStatus("");
            }
        }.start();
    }

    private void setStatus(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                statusLabel.setText(message);
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
                pluginManager.activateMenu();
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_P:
                MusicFile file = (MusicFile) antialiasList.getSelectedValue();
                play(file);
                break;
            case KeyEvent.VK_S:
                stop();
                break;
            case KeyEvent.VK_R:
                scanFiles();
                break;
        }
        antialiasList.processKeyEvent(e);
    }

    private void stop() {
        flacPlayer.stop();
        mp3Player.stop();
        playingFile = null;
        artistLabel.setText("");
        titleLabel.setText("");
        albumLabel.setText("");
        yearLabel.setText("");
        genreLabel.setText("");
    }

    private void play(MusicFile file) {
        flacPlayer.stop();
        mp3Player.stop();

        if (file.isFlac()) {
            flacPlayer.play(file.getFile());
        } else if (file.isMP3()) {
            mp3Player.play(file.getFile());
        }

        artistLabel.setText(file.getArtist());
        titleLabel.setText(file.getTitle());
        albumLabel.setText(file.getAlbum());
        yearLabel.setText(String.valueOf(file.getYear()));
        genreLabel.setText(file.getGenre());

        playingFile = file;
    }

    private void scanFiles() {
        new Thread() {
            public void run() {
                File musicDir = new File("/music");
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

    public MusicFile getPlayingFile() {
        return playingFile;
    }

}
