package limma.plugins.music;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import org.blinkenlights.jid3.ID3Exception;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;

public class MusicPlugin extends JPanel implements Plugin {
    private DefaultListModel fileListModel;
    private JList fileList;
    private FlacPlayer flacPlayer;
    private MP3Player mp3Player;
    private JLabel artistLabel;
    private JLabel titleLabel;
    private JLabel albumLabel;
    private JLabel yearLabel;
    private JLabel genreLabel;

    public MusicPlugin() {
        setOpaque(false);
        setLayout(new BorderLayout(5, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileListModel = new DefaultListModel();
        fileList = new JList(fileListModel);
        JScrollPane scrollPane = new JScrollPane(fileList);
        add(scrollPane, BorderLayout.CENTER);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        fileList.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        JPanel infoPanel = new JPanel();
        add(infoPanel, BorderLayout.SOUTH);
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        infoPanel.setOpaque(false);

        infoPanel.setLayout(new GridLayout(5, 2));
        artistLabel = new JLabel();
        titleLabel = new JLabel();
        albumLabel = new JLabel();
        yearLabel = new JLabel();
        genreLabel = new JLabel();
        artistLabel.setForeground(Color.white);
        titleLabel.setForeground(Color.white);
        albumLabel.setForeground(Color.white);
        yearLabel.setForeground(Color.white);
        genreLabel.setForeground(Color.white);
        infoPanel.add(new JLabel("Artist:"));
        infoPanel.add(artistLabel);
        infoPanel.add(new JLabel("Title:"));
        infoPanel.add(titleLabel);
        infoPanel.add(new JLabel("Album:"));
        infoPanel.add(albumLabel);
        infoPanel.add(new JLabel("Year:"));
        infoPanel.add(yearLabel);
        infoPanel.add(new JLabel("Genre:"));
        infoPanel.add(genreLabel);

        fileList.setCellRenderer(new MusicListCellRenderer());
        flacPlayer = new FlacPlayer();
        mp3Player = new MP3Player();
    }

    public String getPluginName() {
        return "music";
    }

    public JComponent getPluginComponent() {
        return this;
    }

    public void activatePlugin() {
        fileListModel.clear();
        File musicDir = new File("/media/music/Coldplay");
        try {
            scanAndAddFiles(musicDir);
        } catch (ID3Exception e) {
            e.printStackTrace();
        }
        fileList.setSelectedIndex(0);
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.activateMenu();
                break;
            case KeyEvent.VK_ENTER:
                MusicFile file = (MusicFile) fileList.getSelectedValue();
                play(file);
                break;
            case KeyEvent.VK_UP:
                if (fileList.getSelectedIndex() > 0) {
                    fileList.setSelectedIndex(fileList.getSelectedIndex() - 1);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (fileList.getSelectedIndex() < fileListModel.getSize() - 1) {
                    fileList.setSelectedIndex(fileList.getSelectedIndex() + 1);
                }
                break;
        }
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
    }

    private void scanAndAddFiles(File dir) throws ID3Exception {
        ArrayList files = new ArrayList(Arrays.asList(dir.listFiles()));
        Collections.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((File) o1).getName().compareToIgnoreCase(((File) o2).getName());
            }
        });
        for (Iterator i = files.iterator(); i.hasNext();) {
            File file = (File) i.next();
            if (file.isDirectory()) {
                scanAndAddFiles(file);
            } else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".flac")) {
                fileListModel.addElement(new MusicFile(file));
            }
        }
    }

}
