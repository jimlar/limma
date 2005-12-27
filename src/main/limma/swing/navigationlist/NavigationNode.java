package limma.swing.navigationlist;

public interface NavigationNode {
    int getChildCount();

    NavigationNode getChildAt(int index);

    NavigationNode getParent();

    void setParent(NavigationNode parent);

    int getSelectedChildIndex();

    void setSelectedChildIndex(int selectedChildIndex);

    String getTitle();
}
