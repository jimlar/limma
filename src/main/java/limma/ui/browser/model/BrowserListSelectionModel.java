package limma.ui.browser.model;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class BrowserListSelectionModel extends DefaultListSelectionModel {
    private BrowserModel browserModel;
    private int offset;

    public BrowserListSelectionModel(final BrowserModel browserModel, int offset) {
        this.browserModel = browserModel;
        this.offset = offset;

        setSelectionMode(SINGLE_SELECTION);
        setSelectionInterval(0, 0);

        addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (isSelectedIndex(e.getFirstIndex())) {
                    getBaseNode().setSelectedChildIndex(e.getFirstIndex());

                } else {
                    getBaseNode().setSelectedChildIndex(e.getLastIndex());

                }
            }
        });
    }

    private BrowserModelNode getBaseNode() {
        BrowserModelNode baseNode = browserModel.getBaseNode();

        for (int i = 0; i < offset && baseNode != null; i++) {
            baseNode = baseNode.getSelectedChild();
        }

        return baseNode;
    }
}
