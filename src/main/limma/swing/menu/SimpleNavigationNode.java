package limma.swing.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimpleNavigationNode extends NavigationNode {
    private String title;
    private List<NavigationNode> children = new ArrayList<NavigationNode>();

    public SimpleNavigationNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void add(NavigationNode node) {
        children.add(node);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("Cannot add a node that already is added elsewhere!");
        }
        node.setParent(this);
    }


    public void sortByTitle() {
        Collections.sort(children);
    }

    public List<NavigationNode> getChildren() {
        return children;
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

    public void removeAllChildren() {
        children.clear();
    }
}
