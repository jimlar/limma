package limma.plugins.menu;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MenuPlugin extends JPanel implements Plugin {
    private MenuListModel listModel;
    private AntialiasList list;

    public MenuPlugin(PluginManager pluginManager) {
        setOpaque(false);

        MenuNode root = new MenuNode("root");
        root.add(new MenuNode("TV"));
        root.add(new MenuNode("Video"));
        root.add(new PluginNode("Music", "music", pluginManager));
        root.add(new MenuNode("Games"));
        root.add(new MenuNode("Pictures"));
        MenuNode settingsNode = new MenuNode("Settings");
        root.add(settingsNode);
        settingsNode.add(new MenuNode("Appearance"));
        settingsNode.add(new MenuNode("Music Player Settings"));
        settingsNode.add(new MenuNode("Video Settings"));
        settingsNode.add(new MenuNode("News Settings"));

        listModel = new MenuListModel(root);
        list = new AntialiasList(listModel);
        list.setCellRenderer(new MenuCellRenderer());

        list.setSelectedIndex(0);

        setLayout(new GridBagLayout());
        add(list, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    public String getPluginName() {
        return "menu";
    }

    public JComponent getPluginComponent() {
        return this;
    }

    public void pluginEntered() {
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                MenuNode selectedNode = (MenuNode) list.getSelectedValue();
                selectedNode.execute();
                if (!selectedNode.getChildren().isEmpty()) {
                    listModel.setCurrent(selectedNode);
                    list.setSelectedIndex(0);
                }
                break;
            case KeyEvent.VK_ESCAPE:
                MenuNode currentNode = listModel.getCurrent();
                if (currentNode.getParent() == null) {
                    System.exit(0);
                } else {
                    listModel.setCurrent(currentNode.getParent());
                    list.setSelectedIndex(currentNode.getParent().getChildren().indexOf(currentNode));
                }
                break;
        }
        list.processKeyEvent(e);
    }
}
