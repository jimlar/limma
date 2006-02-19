package limma.swing.menu;

import java.util.*;

public class SimpleMenuNode extends MenuNode {
    private String title;
    private List<MenuNode> children = new ArrayList<MenuNode>();

    public SimpleMenuNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void performAction() {
    }

    public void add(MenuNode node) {
        children.add(node);
        if (node.getParent() != null) {
            throw new IllegalArgumentException("Cannot add a node that already is added elsewhere!");
        }
        node.setParent(this);
    }


    public void sortByTitle() {
        Collections.sort(children, new Comparator<MenuNode>() {
            public int compare(MenuNode o1, MenuNode o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public MenuNode getFirstChildWithTitle(String title) {
        for (Iterator<MenuNode> i = children.iterator(); i.hasNext();) {
            MenuNode node = i.next();
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
