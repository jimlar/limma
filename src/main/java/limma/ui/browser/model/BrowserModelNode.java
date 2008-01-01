package limma.ui.browser.model;

import limma.ui.dialogs.DialogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BrowserModelNode implements Comparable {
    private int selectedChildIndex;
    private BrowserModelNode parent;

    public abstract String getTitle();

    public void performAction(DialogManager dialogManager) {
    }

    public List<BrowserModelNode> getChildren() {
        return Collections.emptyList();
    }

    public List<MenuItem> getAllMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>();

        if (parent != null) {
            menuItems.addAll(parent.getAllMenuItems());
        }
        return menuItems;
    }

    public BrowserModelNode getParent() {
        return parent;
    }

    public int getSelectedChildIndex() {
        return selectedChildIndex;
    }

    public void setSelectedChildIndex(int selectedChildIndex) {
        this.selectedChildIndex = selectedChildIndex;
    }

    public void setParent(BrowserModelNode parent) {
        this.parent = parent;
    }

    public BrowserModelNode getSelectedChild() {
        if (getChildren().size() <= selectedChildIndex) {
            return null;
        }
        return getChildren().get(selectedChildIndex);
    }

    public int compareTo(Object o) {
        return getTitle().compareToIgnoreCase(((BrowserModelNode) o).getTitle());
    }
}
