package limma.plugins.menu;

import limma.plugins.menu.*;
import limma.MainWindow;

import javax.swing.*;

public class MenuPlugin extends JList {
    public MenuPlugin(MainWindow mainWindow) {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setSelectedIndex(0);

        addKeyListener(new MenuKeyListener());

        setCellRenderer(new MenuCellRenderer());
        setOpaque(false);

        MenuNode root = new MenuNode("root");
        root.add(new MenuNode("Watch TV"));
        root.add(new MenuNode("Watch Videos"));
        root.add(new PluginNode("Listen to music", "music", mainWindow));
        MenuNode settingsNode = new MenuNode("Settings");
        root.add(settingsNode);
        settingsNode.add(new MenuNode("Appearance"));
        settingsNode.add(new MenuNode("Music Player Settings"));
        settingsNode.add(new MenuNode("Video Settings"));
        settingsNode.add(new MenuNode("News Settings"));

        setModel(new MenuListModel(root));
    }
}
