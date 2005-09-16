package limma;

import limma.plugins.PluginManager;
import limma.plugins.Plugin;
import limma.plugins.menu.MenuPlugin;
import limma.plugins.music.MusicPlugin;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame implements PluginManager {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;
    private Map pluginsByName = new HashMap();

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

        addPlugin(new MenuPlugin(this));
        addPlugin(new MusicPlugin(this));

        showPlugin("menu");
        validate();
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(this);
    }

    public void addPlugin(Plugin plugin) {
        String name = plugin.getPluginName();
        mainPanel.add(plugin.getPluginComponent(), name);
        pluginsByName.put(name, plugin);
    }

    public void showPlugin(String name) {
        Plugin plugin = (Plugin) pluginsByName.get(name);
        plugin.activatePlugin();
        pluginCardsManager.show(mainPanel, name);
    }
}
