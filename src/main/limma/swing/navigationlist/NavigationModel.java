package limma.swing.navigationlist;

import limma.swing.navigationlist.NavigationNode;

import javax.swing.*;

public class NavigationModel extends AbstractListModel {
    private NavigationNode root = new NavigationNode("root");
    private NavigationNode currentNode = root;

    public void add(NavigationNode node) {
        root.add(node);
    }

    public void setCurrentNode(NavigationNode currentNode) {
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

    public NavigationNode getCurrentNode() {
        return currentNode;
    }
}
