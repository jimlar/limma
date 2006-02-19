package limma.swing.menu;

import limma.swing.menu.SimpleMenuNode;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class MenuModel extends AbstractListModel {
    private Set<MenuModelListener> listeners = new HashSet<MenuModelListener>();
    private SimpleMenuNode root = new SimpleMenuNode(null);
    private MenuNode currentNode = root;

    public void add(MenuNode node) {
        root.add(node);
    }

    public void setCurrentNode(MenuNode currentNode) {
        if (!this.currentNode.getChildren().isEmpty()) {
            fireIntervalRemoved(this, 0, this.currentNode.getChildren().size() - 1);
        }
        this.currentNode = currentNode;
        if (!this.currentNode.getChildren().isEmpty()) {
            fireIntervalAdded(this, 0, this.currentNode.getChildren().size() - 1);
        }
        fireCurrentNodeChanged();
    }

    public int getSize() {
        return currentNode.getChildren().size();
    }

    public Object getElementAt(int index) {
        return currentNode.getChildren().get(index);
    }

    public MenuNode getCurrentNode() {
        return currentNode;
    }

    public void addMenuListener(MenuModelListener listener) {
        listeners.add(listener);
    }

    public MenuNode getRoot() {
        return root;
    }

    private void fireCurrentNodeChanged() {
        for (Iterator<MenuModelListener> i = listeners.iterator(); i.hasNext();) {
            MenuModelListener listener = i.next();
            listener.currentNodeChanged(this, currentNode);
        }
    }
}
