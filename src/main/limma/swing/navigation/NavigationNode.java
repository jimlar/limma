package limma.swing.navigation;

import limma.swing.DialogManager;

import java.util.Collections;
import java.util.List;

public abstract class NavigationNode implements Comparable {
    private int selectedChildIndex;
    private NavigationNode parent;

    public abstract String getTitle();

    public void performAction(DialogManager dialogManager) {
    }

    public List<NavigationNode> getChildren() {
        return Collections.emptyList();
    }

    public List<MenuItem> getMenuItems() {
        return Collections.emptyList();
    }

    public NavigationNode getParent() {
        return parent;
    }

    public int getSelectedChildIndex() {
        return selectedChildIndex;
    }

    public void setSelectedChildIndex(int selectedChildIndex) {
        this.selectedChildIndex = selectedChildIndex;
    }

    public void setParent(NavigationNode parent) {
        this.parent = parent;
    }

    public NavigationNode getSelectedChild() {
        return getChildren().get(selectedChildIndex);
    }

    public int compareTo(Object o) {
        return getTitle().compareToIgnoreCase(((NavigationNode) o).getTitle());
    }
}
