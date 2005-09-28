package limma;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.menu.MenuPlugin;
import limma.swing.ImagePanel;

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

    public MainWindow(Plugin[] plugins) {
        ImageIcon background = new ImageIcon("background.jpg");
        mainPanel = new ImagePanel(background);
        mainPanel.setOpaque(false);
        pluginCardsManager = new CardLayout();
        mainPanel.setLayout(pluginCardsManager);
        this.setContentPane(mainPanel);

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    getCurrentPlugin().keyPressed(e, MainWindow.this);
                }
                return true;
            }
        });

        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            addPlugin(plugin);
        }
        addPlugin(new MenuPlugin(this));
        enterPlugin("menu");
        validate();
    }

    private Plugin getCurrentPlugin() {
        return currentPlugin;
    }

    public void addPlugin(Plugin plugin) {
        String name = plugin.getPluginName();
        JComponent pluginComponent = plugin.getPluginComponent();
        pluginComponent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.add(pluginComponent, name);
        pluginsByName.put(name, plugin);
    }

    public void enterPlugin(String name) {
        Plugin plugin = (Plugin) pluginsByName.get(name);
        currentPlugin = plugin;
        plugin.pluginEntered();
        pluginCardsManager.show(mainPanel, name);
    }

    public void exitPlugin() {
        enterPlugin("menu");
    }
}
