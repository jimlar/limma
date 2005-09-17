package limma;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.menu.MenuPlugin;
import limma.plugins.music.MusicPlugin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame implements PluginManager {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;
    private Map pluginsByName = new HashMap();
    private Plugin currentPlugin;

    public MainWindow() {
        ImageIcon background = new ImageIcon("background.jpg");
        mainPanel = new ImagePanel(background);
        mainPanel.setOpaque(false);
        pluginCardsManager = new CardLayout();
        mainPanel.setLayout(pluginCardsManager);
        this.setContentPane(mainPanel);

        addPlugin(new MenuPlugin(this));
        addPlugin(new MusicPlugin());
        activatePlugin("menu");

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    getCurrentPlugin().keyPressed(e, MainWindow.this);
                }
                return true;
            }
        });
        validate();
    }

    private Plugin getCurrentPlugin() {
        return currentPlugin;
    }

    public void addPlugin(Plugin plugin) {
        String name = plugin.getPluginName();
        mainPanel.add(plugin.getPluginComponent(), name);
        pluginsByName.put(name, plugin);
    }

    public void activatePlugin(String name) {
        Plugin plugin = (Plugin) pluginsByName.get(name);
        currentPlugin = plugin;
        plugin.activatePlugin();
        pluginCardsManager.show(mainPanel, name);
    }

    public void activateMenu() {
        activatePlugin("menu");
    }
}
