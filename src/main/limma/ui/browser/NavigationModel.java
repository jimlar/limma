package limma.ui.browser;

import javax.swing.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NavigationModel extends AbstractListModel {
    private Set<NavigationModelListener> listeners = new HashSet<NavigationModelListener>();
    private SimpleNavigationNode root = new SimpleNavigationNode("Limma");
    private NavigationNode currentNode = root;

    public void add(MenuItem menuItem) {
        root.add(menuItem);
    }

    public void add(NavigationNode node) {
        root.add(node);
    }

    public void setCurrentNode(NavigationNode newNode) {
        NavigationNode oldNode = this.currentNode;

        if (!oldNode.getChildren().isEmpty()) {
            fireIntervalRemoved(this, 0, oldNode.getChildren().size() - 1);
        }
        this.currentNode = newNode;
        if (!this.currentNode.getChildren().isEmpty()) {
            fireIntervalAdded(this, 0, this.currentNode.getChildren().size() - 1);
        }
        fireCurrentNodeChanged(oldNode, newNode);
    }

    public int getSize() {
        return currentNode.getChildren().size();
    }

    public Object getElementAt(int index) {
        return currentNode.getChildren().get(index);
    }

    public NavigationNode getCurrentNode() {
        return currentNode;
    }

    public void addMenuListener(NavigationModelListener listener) {
        listeners.add(listener);
    }

    public NavigationNode getRoot() {
        return root;
    }

    private void fireCurrentNodeChanged(NavigationNode oldNode, NavigationNode newNode) {
        for (Iterator<NavigationModelListener> i = listeners.iterator(); i.hasNext();) {
            NavigationModelListener listener = i.next();
            listener.currentNodeChanged(this, oldNode, newNode);
        }
    }
}
