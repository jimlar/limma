package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.*;
import limma.swing.navigationlist.NavigationModel;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.swing.navigationlist.NavigationList;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;


public class VideoPlugin implements Plugin {
    private SimpleListModel videoListModel;
    private boolean hasEntered;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private VideoConfig videoConfig;
    private NavigationModel navigationModel;
    private AntialiasList videoList;
    private VideoPlayer videoPlayer;
    private MenuDialog popupMenu;
    private DefaultNavigationNode moviesNode;

    public VideoPlugin(final DialogManager dialogManager, final PersistenceManager persistenceManager, final IMDBSevice imdbSevice, final VideoConfig videoConfig, VideoPlayer videoPlayer, NavigationModel navigationModel, NavigationList navigationList) {
        this.dialogManager = dialogManager;
        this.persistenceManager = persistenceManager;
        this.videoConfig = videoConfig;
        this.navigationModel = navigationModel;
        persistenceManager.addPersistentClass(Video.class);
        moviesNode = new DefaultNavigationNode("Movies");
        this.videoPlayer = videoPlayer;

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
        navigationList.addCellRenderer(new VideoListCellRenderer(videoConfig));
    }

    public void init() {
        navigationModel.add(moviesNode);
        dialogManager.executeInDialog(new LoadVideosTask(persistenceManager, moviesNode, videoPlayer, dialogManager));
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
    }

    public boolean keyPressed(KeyEvent e, PluginManager pluginManager) {
        return false;
    }

    private void scanForVideos() {
        dialogManager.executeInDialog(new ScanForVideosTask(this, persistenceManager, videoConfig));
    }

    public void reloadVideos() {
        dialogManager.executeInDialog(new LoadVideosTask(persistenceManager, moviesNode, videoPlayer, dialogManager));
    }
}
