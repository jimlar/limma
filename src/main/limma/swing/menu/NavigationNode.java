package limma.swing.menu;

import java.util.List;

public abstract class NavigationNode {
    private int selectedChildIndex;
    private NavigationNode parent;

    public abstract String getTitle();

    public abstract void performAction();

    public abstract List<NavigationNode> getChildren();

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
}
