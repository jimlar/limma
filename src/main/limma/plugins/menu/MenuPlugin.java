package limma.plugins.menu;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class MenuPlugin extends JList implements Plugin {
    private MenuListModel listModel;

    public MenuPlugin(PluginManager pluginManager) {
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setCellRenderer(new MenuCellRenderer());
        setOpaque(false);

        MenuNode root = new MenuNode("root");
        root.add(new MenuNode("Watch TV"));
        root.add(new MenuNode("Watch Videos"));
        root.add(new PluginNode("Listen to music", "music", pluginManager));
        MenuNode settingsNode = new MenuNode("Settings");
        root.add(settingsNode);
        settingsNode.add(new MenuNode("Appearance"));
        settingsNode.add(new MenuNode("Music Player Settings"));
        settingsNode.add(new MenuNode("Video Settings"));
        settingsNode.add(new MenuNode("News Settings"));

        listModel = new MenuListModel(root);
        setModel(listModel);
        setSelectedIndex(0);
    }

    public String getPluginName() {
        return "menu";
    }

    public JComponent getPluginComponent() {
        return this;
    }

    public void activatePlugin() {
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                MenuNode selectedNode = (MenuNode) getSelectedValue();
                selectedNode.execute();
                if (!selectedNode.getChildren().isEmpty()) {
                    listModel.setCurrent(selectedNode);
                    setSelectedIndex(0);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                MenuNode currentNode = listModel.getCurrent();
                if (currentNode.getParent() == null) {
                    System.exit(0);
                } else {
                    listModel.setCurrent(currentNode.getParent());
                    setSelectedIndex(currentNode.getParent().getChildren().indexOf(currentNode));
                }
                break;
            case KeyEvent.VK_UP:
                if (getSelectedIndex() > 0) {
                    setSelectedIndex(getSelectedIndex() - 1);
                }
                break;
            case KeyEvent.VK_DOWN:
                if (getSelectedIndex() < listModel.getSize() - 1) {
                    setSelectedIndex(getSelectedIndex() + 1);
                }
                break;
        }
    }
}
