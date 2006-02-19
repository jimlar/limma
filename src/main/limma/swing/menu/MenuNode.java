package limma.swing.menu;

import java.util.List;

public abstract class MenuNode {
    private int selectedChildIndex;
    private MenuNode parent;

    public abstract String getTitle();

    public abstract void performAction();

    public abstract List<MenuNode> getChildren();

    public MenuNode getParent() {
        return parent;
    }

    public int getSelectedChildIndex() {
        return selectedChildIndex;
    }

    public void setSelectedChildIndex(int selectedChildIndex) {
        this.selectedChildIndex = selectedChildIndex;
    }

    public void setParent(MenuNode parent) {
        this.parent = parent;
    }

    public MenuNode getSelectedChild() {
        return getChildren().get(selectedChildIndex);
    }
}
