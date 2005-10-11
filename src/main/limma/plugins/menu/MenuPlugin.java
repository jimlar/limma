package limma.plugins.menu;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.LimmaMenu;
import limma.swing.MenuNode;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuPlugin implements Plugin {
    private LimmaMenu limmaMenu;

    public MenuPlugin(PluginManager pluginManager) {
        MenuNode root = new MenuNode("root") {
            public void execute() {
                System.exit(0);
            }
        };
        root.add(new MenuNode("TV"));
        root.add(new PluginNode("Video", "video", pluginManager));
        root.add(new PluginNode("Music", "music", pluginManager));
        root.add(new PluginNode("Games", "game", pluginManager));
        root.add(new MenuNode("Pictures"));
        MenuNode settingsNode = new MenuNode("Settings");
        root.add(settingsNode);
        settingsNode.add(new MenuNode("Appearance"));
        settingsNode.add(new MenuNode("Music Player Settings"));
        settingsNode.add(new MenuNode("Video Settings"));
        settingsNode.add(new MenuNode("News Settings"));

        limmaMenu = new LimmaMenu(root);
    }

    public String getPluginName() {
        return "menu";
    }

    public JComponent getPluginView() {
        return limmaMenu;
    }

    public void pluginEntered() {
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        limmaMenu.keyPressed(e);
    }
}
