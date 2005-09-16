package limma;

import limma.plugins.menu.MenuPlugin;
import limma.plugins.music.MusicPlugin;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;

    public MainWindow() {
        super(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());

        this.setUndecorated(true);
        this.setResizable(false);

        ImageIcon background = new ImageIcon("background.jpg");
        mainPanel = new ImagePanel(background);
        mainPanel.setOpaque(false);
        pluginCardsManager = new CardLayout();
        mainPanel.setLayout(pluginCardsManager);
        this.setContentPane(mainPanel);

        addPlugin("menu", new MenuPlugin(this));
        addPlugin("music", new MusicPlugin(this));

        showPlugin("menu");
        validate();
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
    }

    public void addPlugin(String name, JComponent comp) {
        mainPanel.add(comp, name);
    }

    public void showPlugin(String plugin) {
        pluginCardsManager.show(mainPanel, plugin);
    }

}
