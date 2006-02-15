package limma.swing.navigationlist;

import limma.swing.navigationlist.MenuNode;

import javax.swing.*;

public class MenuModel extends AbstractListModel {
    private MenuNode root = new MenuNode("root");
    private MenuNode currentNode = root;

    public void add(MenuNode node) {
        root.add(node);
    }

    public void setCurrentNode(MenuNode currentNode) {
        if (this.currentNode.getChildCount() > 0) {
            fireIntervalRemoved(this, 0, this.currentNode.getChildCount() - 1);
        }
        this.currentNode = currentNode;
        if (this.currentNode.getChildCount() > 0) {
            fireIntervalAdded(this, 0, this.currentNode.getChildCount() - 1);
        }
    }

    public int getSize() {
        return currentNode.getChildCount();
    }

    public Object getElementAt(int index) {
        return currentNode.getChildAt(index);
    }

    public MenuNode getCurrentNode() {
        return currentNode;
    }
}
