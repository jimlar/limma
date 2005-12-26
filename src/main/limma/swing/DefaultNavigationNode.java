package limma.swing;

import java.util.ArrayList;
import java.util.List;

public class DefaultNavigationNode implements NavigationNode {
    private String title;
    private NavigationNode parent;
    private List<NavigationNode> children = new ArrayList<NavigationNode>();

    public DefaultNavigationNode(String title) {
        this.title = title;
    }

    public void add(NavigationNode node) {
        children.add(node);
        node.setParent(this);
    }

    public String toString() {
        return title;
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
}
