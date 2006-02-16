package limma.swing.menu;

import limma.swing.menu.MenuNode;

import javax.swing.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class MenuModel extends AbstractListModel {
    private Set<MenuModelListener> listeners = new HashSet<MenuModelListener>();
    private MenuNode root = new MenuNode(null);
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
        fireCurrentNodeChanged();
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
