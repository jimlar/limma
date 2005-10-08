package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasLabel;
import limma.swing.AntialiasList;
import limma.swing.DialogManager;
import limma.swing.SimpleListModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;


public class VideoPlugin implements Plugin {
    private SimpleListModel videoListModel;
    private boolean hasEntered;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private AntialiasList videoList;

    public VideoPlugin(DialogManager dialogManager, PersistenceManager persistenceManager) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        persistenceManager.addPersistentClass(Video.class);
    }

    public String getPluginName() {
        return "video";
    }

    public JComponent getPluginComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        TitledBorder titledBorder = BorderFactory.createTitledBorder("Videos");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        panel.setBorder(titledBorder);

        videoListModel = new SimpleListModel();
        videoList = new AntialiasList(videoListModel);
        JScrollPane scrollPane = new JScrollPane(videoList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBorder(null);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    public void pluginEntered() {
        if (!hasEntered) {
            hasEntered = true;
            reloadVideos();
        }
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
            case KeyEvent.VK_R:
                scanForVideos();
                break;
            default:
                videoList.processKeyEvent(e);
        }
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager));
    }

    void setVideos(java.util.List videos) {
        videoListModel.setObjects(videos);
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(this, persistenceManager));
    }
}
