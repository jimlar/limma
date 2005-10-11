package limma.swing;

import limma.plugins.menu.MenuCellRenderer;
import limma.plugins.menu.MenuListModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LimmaMenu extends JPanel {
    private MenuListModel listModel;
    private AntialiasList list;
    private MenuNode root;

    public LimmaMenu(MenuNode root) {
        this.root = root;
        setOpaque(false);
        listModel = new MenuListModel(root);
        list = new AntialiasList(listModel);
        list.setCellRenderer(new MenuCellRenderer());

        list.setSelectedIndex(0);

        setLayout(new GridBagLayout());
        add(list, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void keyPressed(KeyEvent e) {
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
                    root.execute();
                } else {
                    listModel.setCurrent(currentNode.getParent());
                    list.setSelectedIndex(currentNode.getParent().getChildren().indexOf(currentNode));
                }
                break;
            default:
                list.processKeyEvent(e);
        }
    }
}
