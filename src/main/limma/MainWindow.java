package limma;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.plugins.menu.MenuPlugin;
import limma.swing.DialogManager;
import limma.swing.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class MainWindow extends JFrame implements PluginManager {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;
    private Map pluginsByName = new HashMap();
    private Plugin currentPlugin;

    public MainWindow(GraphicsDevice graphicsDevice, DialogManager dialogManager, Plugin[] plugins) {
        ImageIcon background = new ImageIcon("background.jpg");

        this.setContentPane(dialogManager.getDialogManagerComponent());

        mainPanel = new ImagePanel(background);
        mainPanel.setOpaque(false);
        pluginCardsManager = new CardLayout(10, 10);
        mainPanel.setLayout(pluginCardsManager);
        dialogManager.setRoot(mainPanel);
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
        setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), new Point(0, 0), "invisible"));
        validate();
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            addPlugin(plugin);
        }
    }

    private void addPlugin(Plugin plugin) {
        String name = plugin.getPluginName();
        mainPanel.add(plugin.getPluginComponent(), name);
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
