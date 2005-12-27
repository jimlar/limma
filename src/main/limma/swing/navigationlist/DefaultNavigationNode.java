package limma.swing.navigationlist;

import limma.swing.navigationlist.NavigationNode;

import java.util.ArrayList;
import java.util.List;

public class DefaultNavigationNode implements NavigationNode {
    private String title;
    private NavigationNode parent;
    private List<NavigationNode> children = new ArrayList<NavigationNode>();
    private int selectedChildIndex;

    public DefaultNavigationNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void add(NavigationNode node) {
        children.add(node);
        node.setParent(this);
    }

    public int getChildCount() {
        return children.size();
    }

    public NavigationNode getChildAt(int index) {
        return children.get(index);
    }

    public NavigationNode getParent() {
        return parent;
    }

    public void setParent(NavigationNode parent) {
        this.parent = parent;
    }

    public int getSelectedChildIndex() {
        return selectedChildIndex;
    }

    public void setSelectedChildIndex(int selectedChildIndex) {
        this.selectedChildIndex = selectedChildIndex;
    }
}
