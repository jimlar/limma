package limma.ui.browser.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SimpleBrowserNode extends BrowserModelNode {
    private String title;
    private List<BrowserModelNode> children = new ArrayList<BrowserModelNode>();
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();

    public SimpleBrowserNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void add(BrowserModelNode node) {
        children.add(node);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("Cannot add a node that already is added elsewhere!");
        }
        node.setParent(this);
    }

    public void add(MenuItem menuItem) {
        menuItems.add(menuItem);
    }

    public List<MenuItem> getAllMenuItems() {
        ArrayList<MenuItem> menuItems = new ArrayList<MenuItem>(this.menuItems);

        if (getParent() != null) {
            menuItems.addAll(getParent().getAllMenuItems());
        }
        return menuItems;
    }

    public void sort() {
        Collections.sort(children);
    }

    public List<BrowserModelNode> getChildren() {
        return children;
    }

    public BrowserModelNode getFirstChildWithTitle(String title) {
        for (BrowserModelNode node : children) {
            if (title.equals(node.getTitle())) {
                return node;
            }
        }
        return null;
    }

    public void removeAllChildren() {
        children.clear();
    }

    public String toString() {
        return new ToStringBuilder(this).append("title", title).toString();

    }

    protected void sort(Comparator<BrowserModelNode> comparator) {
        Collections.sort(children, comparator);
    }
}
