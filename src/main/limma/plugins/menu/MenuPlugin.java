package limma.plugins.menu;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.LimmaMenu;
import limma.swing.LimmaMenuItem;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuPlugin implements Plugin {
    private LimmaMenu limmaMenu;

    public MenuPlugin(PluginManager pluginManager) {
        limmaMenu = new LimmaMenu(new Runnable() {
            public void run() {
                System.exit(0);
            }

        });
        limmaMenu.add(new LimmaMenuItem("TV"));
        limmaMenu.add(new PluginItem("Video", "video", pluginManager));
        limmaMenu.add(new PluginItem("Music", "music", pluginManager));
        limmaMenu.add(new PluginItem("Games", "game", pluginManager));
        limmaMenu.add(new LimmaMenuItem("Pictures"));
        limmaMenu.select(0);
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
