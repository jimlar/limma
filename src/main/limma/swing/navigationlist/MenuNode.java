package limma.swing.navigationlist;

import java.util.*;

public class MenuNode {
    private String title;
    private MenuNode parent;
    private List<MenuNode> children = new ArrayList<MenuNode>();
    private int selectedChildIndex;

    public MenuNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void performAction() {
    }

    public void add(MenuNode node) {
        children.add(node);
        assert node.getParent() == null : "Cannot add a node that already is added elsewhere!";
        node.setParent(this);
    }

    public void add(int index, MenuNode node) {
        children.add(index, node);
        node.setParent(this);
    }


    public void sortByTitle() {
        Collections.sort(children, new Comparator<MenuNode>() {
            public int compare(MenuNode o1, MenuNode o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
    }

    public int getChildCount() {
        return children.size();
    }

    public MenuNode getChildAt(int index) {
        return children.get(index);
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

    public MenuNode getParent() {
        return parent;
    }

    public void setParent(MenuNode parent) {
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
