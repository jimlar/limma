package limma.plugins.menu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JList;

import limma.plugins.menu.MenuNode;
import limma.plugins.menu.MenuListModel;

public class MenuKeyListener extends KeyAdapter {

    public void keyPressed(KeyEvent e) {
        JList list = (JList) e.getSource();
        MenuListModel listModel = (MenuListModel) list.getModel();

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            MenuNode node = (MenuNode) list.getSelectedValue();
            node.execute();
            if (!node.getChildren().isEmpty()) {
                listModel.setCurrent(node);
                list.setSelectedIndex(0);
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            MenuNode node = listModel.getCurrent();
            if (node.getParent() == null) {
                System.exit(0);
            } else {
                listModel.setCurrent(node.getParent());
                list.setSelectedIndex(node.getParent().getChildren().indexOf(node));
            }
        }
    }
}
