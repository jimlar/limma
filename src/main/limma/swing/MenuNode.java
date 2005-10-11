package limma.swing;

import java.util.ArrayList;
import java.util.List;

public class MenuNode {
    private MenuNode parent;
    private String title;
    private List children = new ArrayList();

    public MenuNode(MenuNode parent, String title) {
        this.parent = parent;
        this.title = title;
    }

    public MenuNode(String title) {
        this(null, title);
    }

    public void execute() {
    }

    public List getChildren() {
        return children;
    }

    public void add(MenuNode node) {
        children.add(node);
        node.setParent(this);
    }

    public void removeChildren() {
        children.clear();
    }

    public String toString() {
        return title;
    }

    public String getTitle() {
        return title;
    }

    public MenuNode getParent() {
        return parent;
    }

    public void setParent(MenuNode parent) {
        this.parent = parent;
    }
}
