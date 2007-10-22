package limma.ui.browser;

import javax.swing.*;

public class RightBrowserListModel extends AbstractListModel {
    private NavigationModel navigationModel;

    public RightBrowserListModel(NavigationModel navigationModel) {
        this.navigationModel = navigationModel;

        navigationModel.addMenuListener(new NavigationModelListener() {
            public void currentNodeChanged(NavigationModel navigationModel, NavigationNode oldNode, NavigationNode newNode) {
                currentNodeChanged(oldNode, newNode);
            }

            private void currentNodeChanged(NavigationNode oldNode, NavigationNode newNode) {
                oldNode = getCurrentNode(oldNode);
                newNode = getCurrentNode(newNode);

                if (!oldNode.getChildren().isEmpty()) {
                    fireIntervalRemoved(this, 0, oldNode.getChildren().size() - 1);
                }
                if (!newNode.getChildren().isEmpty()) {
                    fireIntervalAdded(this, 0, newNode.getChildren().size() - 1);
                }
            }
        });
    }

    public int getSize() {
        return getCurrentNode().getChildren().size();
    }

    private NavigationNode getCurrentNode() {
        return getCurrentNode(navigationModel.getCurrentNode());
    }

    private NavigationNode getCurrentNode(NavigationNode currentNavModelNode) {
        return currentNavModelNode.getSelectedChild();
    }

    public Object getElementAt(int index) {
        return getCurrentNode().getChildren().get(index);
    }

    public void fireContentsChanged() {
        fireIntervalRemoved(this, 0, Integer.MAX_VALUE);
        fireIntervalAdded(this, 0, getSize() - 1);
    }
}
