package limma.ui.browser;

import javax.swing.*;

public class RightBrowserListModel extends AbstractListModel {
    private NavigationModel navigationModel;

    public RightBrowserListModel(NavigationModel navigationModel) {
        this.navigationModel = navigationModel;
    }

    public int getSize() {
        return getCurrentNode().getChildren().size();
    }

    private NavigationNode getCurrentNode() {
        return navigationModel.getCurrentNode().getSelectedChild();
    }

    public Object getElementAt(int index) {
        return getCurrentNode().getChildren().get(index);
    }
}
