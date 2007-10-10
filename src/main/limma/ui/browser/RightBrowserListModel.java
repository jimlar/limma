package limma.ui.browser;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

public class RightBrowserListModel extends AbstractListModel {
    private NavigationModel navigationModel;

    public RightBrowserListModel(NavigationModel navigationModel) {
        this.navigationModel = navigationModel;
        navigationModel.addListDataListener(new ListDataListener() {
            public void intervalAdded(ListDataEvent e) {
                fireIntervalAdded(e.getSource(), e.getIndex0(), e.getIndex1());
            }
            public void intervalRemoved(ListDataEvent e) {
                fireIntervalRemoved(e.getSource(), e.getIndex0(), e.getIndex1());
            }
            public void contentsChanged(ListDataEvent e) {
                fireContentsChanged(e.getSource(), e.getIndex0(), e.getIndex1());
            }
        });
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
