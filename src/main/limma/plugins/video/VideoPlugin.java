package limma.plugins.video;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.*;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;


public class VideoPlugin implements Plugin {
    private SimpleListModel videoListModel;
    private boolean hasEntered;
    private DialogManager dialogManager;
    private AntialiasList videoList;

    public VideoPlugin(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
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
            dialogManager.executeInDialog(new Task() {
                public JComponent createComponent() {
                    return new AntialiasLabel("Loading video list...");
                }

                public void run() {
                    final ArrayList videos = new ArrayList();
                    new DirectoryScanner(new File("/media/movies")).accept(new DirectoryScanner.Visitor() {
                        public void visit(File file) {
                            videos.add(file);
                        }
                    });
                    videoListModel.setObjects(videos);
                }
            });
        }
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
        }
        videoList.processKeyEvent(e);
    }
}
