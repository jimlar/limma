package limma.swing.navigation;

import javax.swing.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NavigationModel extends AbstractListModel {
    private Set<NavigationModelListener> listeners = new HashSet<NavigationModelListener>();
    private SimpleNavigationNode root = new SimpleNavigationNode(null);
    private NavigationNode currentNode = root;

    public void add(MenuItem menuItem) {
        root.add(menuItem);
    }

    public void add(NavigationNode node) {
        root.add(node);
    }

    public void setCurrentNode(NavigationNode currentNode) {
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

    public NavigationNode getCurrentNode() {
        return currentNode;
    }

    public void addMenuListener(NavigationModelListener listener) {
        listeners.add(listener);
    }

    public NavigationNode getRoot() {
        return root;
    }

    private void fireCurrentNodeChanged() {
        for (Iterator<NavigationModelListener> i = listeners.iterator(); i.hasNext();) {
            NavigationModelListener listener = i.next();
            listener.currentNodeChanged(this, currentNode);
        }
    }
}
