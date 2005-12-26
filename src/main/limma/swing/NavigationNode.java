package limma.swing;

public interface NavigationNode {
    int getChildCount();

    NavigationNode getChildAt(int index);

    NavigationNode getParent();

    void setParent(NavigationNode parent);
}
