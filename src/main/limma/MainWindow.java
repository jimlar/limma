package limma;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.menu.MenuPlugin;
import limma.swing.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainWindow extends JFrame implements PluginManager {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;
    private Map pluginsByName = new HashMap();
    private Plugin currentPlugin;

    public MainWindow(GraphicsDevice graphicsDevice, JDesktopPane desktopPane) {
        ImageIcon background = new ImageIcon("background.jpg");

        this.setContentPane(desktopPane);
        desktopPane.setOpaque(false);

        mainPanel = new ImagePanel(background);
        mainPanel.setOpaque(false);
        pluginCardsManager = new CardLayout();
        mainPanel.setLayout(pluginCardsManager);
        desktopPane.add(mainPanel, new Integer(Integer.MIN_VALUE));
        mainPanel.setSize(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    currentPlugin.keyPressed(e, MainWindow.this);
                }
                return true;
            }
        });

        addPlugin(new MenuPlugin(this));
        enterPlugin("menu");
        validate();
    }

    public void addPlugins(Collection plugins) {
        for (Iterator iterator = plugins.iterator(); iterator.hasNext();) {
            Plugin plugin = (Plugin) iterator.next();
            addPlugin(plugin);
        }
    }

    private void addPlugin(Plugin plugin) {
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
