package limma.ui.browser.model;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class BrowserListModel extends AbstractListModel {
    private BrowserModel browserModel;
    private int offset;

    public BrowserListModel(BrowserModel browserModel, int offset) {
        this.browserModel = browserModel;
        this.offset = offset;

        browserModel.addListener(new BrowserModelListener() {
            public void currentNodeChanged(BrowserModel browserModel, BrowserModelNode oldNode, BrowserModelNode newNode) {
                fireContentsChanged();
            }
        });
    }

    protected void fireContentsChanged() {
        fireContentsChanged(this, 0, Math.max(getSize() - 1, 0));
    }

    public int getSize() {
        return getElements().size();
    }

    public Object getElementAt(int index) {
        return getElements().get(index);
    }

    private List getElements() {
        BrowserModelNode baseNode = browserModel.getBaseNode();

        for (int i = 0; i < offset && baseNode != null; i++) {
            baseNode = baseNode.getSelectedChild();
        }

        if (baseNode == null) {
            return Collections.EMPTY_LIST;
        }
        return baseNode.getChildren();
    }
}
