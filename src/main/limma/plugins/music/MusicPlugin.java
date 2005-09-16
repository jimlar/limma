package limma.plugins.music;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import org.blinkenlights.jid3.ID3Exception;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;

public class MusicPlugin extends JPanel implements Plugin {
    private DefaultListModel fileListModel;

    public MusicPlugin(final PluginManager pluginManager) {
        setOpaque(false);
        setLayout(new BorderLayout(5, 20));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fileListModel = new DefaultListModel();
        JList fileList = new JList(fileListModel);
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

        fileList.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    pluginManager.showPlugin("menu");
                }
            }
        });
        fileList.setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                MusicFile file = (MusicFile) value;
                JLabel label = new JLabel(file.getArtist() + " - " + file.getTitle());
                label.setForeground(Color.white);
                if (isSelected) {
                    label.setBorder(BorderFactory.createLineBorder(Color.white, 1));
                }
                return label;
            }
        });
    }

    public String getPluginName() {
        return "music";
    }

    public JComponent getPluginComponent() {
        return this;
    }

    public void activatePlugin() {
        fileListModel.clear();
        File musicDir = new File("/media/music");
        try {
            scanAndAddFiles(musicDir);
        } catch (ID3Exception e) {
            e.printStackTrace();
        }
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
            } else if (file.getName().endsWith(".mp3") || file.getName().endsWith(".flac")){
                fileListModel.addElement(new MusicFile(file));
            }
        }
    }
}
