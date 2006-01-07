package limma.swing.navigationlist;

import java.util.*;

public class NavigationNode {
    private String title;
    private NavigationNode parent;
    private List<NavigationNode> children = new ArrayList<NavigationNode>();
    private int selectedChildIndex;

    public NavigationNode(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void performAction() {
    }

    public void add(NavigationNode node) {
        children.add(node);
        assert node.getParent() == null : "Cannot add a node that already is added elsewhere!";
        node.setParent(this);
    }

    public void add(int index, NavigationNode node) {
        children.add(index, node);
        node.setParent(this);
    }


    public void sortByTitle() {
        Collections.sort(children, new Comparator<NavigationNode>() {
            public int compare(NavigationNode o1, NavigationNode o2) {
                return o1.getTitle().compareToIgnoreCase(o2.getTitle());
            }
        });
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
