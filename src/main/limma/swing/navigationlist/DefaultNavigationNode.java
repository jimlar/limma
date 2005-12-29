package limma.swing.navigationlist;

import java.util.ArrayList;
import java.util.Iterator;
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

    public void performAction() {
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

    public NavigationNode getFirstChildWithTitle(String title) {
        for (Iterator<NavigationNode> i = children.iterator(); i.hasNext();) {
            NavigationNode node = i.next();
            if (title.equals(node.getTitle())) {
                return node;
            }
        }
        return null;
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

    public void removeAllChildren() {
        children.clear();
    }
}
