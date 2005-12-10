package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;


public class VideoPlugin implements Plugin {
    private SimpleListModel videoListModel;
    private boolean hasEntered;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private VideoConfig videoConfig;
    private AntialiasList videoList;
    private VideoPlayer videoPlayer;
    private MenuDialog popupMenu;

    public VideoPlugin(final DialogManager dialogManager, final PersistenceManager persistenceManager, final IMDBSevice imdbSevice, final VideoConfig videoConfig) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.videoConfig = videoConfig;
        persistenceManager.addPersistentClass(Video.class);
        videoPlayer = new VideoPlayer(videoConfig);

        popupMenu = new MenuDialog(dialogManager);
        popupMenu.addItem(new LimmaMenuItem("Scan for new videos") {
            public void execute() {
                scanForVideos();
            }
        });
        popupMenu.addItem(new LimmaMenuItem("Update from IMDB") {
            public void execute() {
                Video selectedVideo = (Video) videoList.getSelectedValue();
                if (selectedVideo != null) {
                    IMDBDialog imdbDialog = new IMDBDialog(dialogManager, selectedVideo, imdbSevice, persistenceManager, videoConfig);
                    imdbDialog.open();
                }
            }
        });
    }

    public String getPluginName() {
        return "video";
    }

    public JComponent getPluginView() {
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
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollPane, BorderLayout.CENTER);

        videoList.setCellRenderer(new VideoListCellRenderer(videoConfig));
        

        return panel;
    }

    public void pluginEntered() {
        if (!hasEntered) {
            hasEntered = true;
            reloadVideos();
        }
    }

    public boolean keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
            case KeyEvent.VK_ENTER:
                play((Video) videoList.getSelectedValue());
                break;
            case KeyEvent.VK_M:
                popupMenu.open();
                break;
            default:
                videoList.processKeyEvent(e);
        }
        return true;
    }

    private void play(final Video video) {
        if (video == null) {
            return;
        }

        dialogManager.executeInDialog(new Task() {
            public JComponent createComponent() {
                return new AntialiasLabel("Playing " + video.getTitle() + "...");
            }

            public void run() {
                try {
                    videoPlayer.play(video);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig));
    }

    void setVideos(final java.util.List videos) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                videoListModel.setObjects(videos);
            }
        });
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(this, persistenceManager));
    }

}
