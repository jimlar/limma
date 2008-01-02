package limma.ui.browser.model;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.HashSet;
import java.util.Set;

public class BrowserModel {
    private Set<BrowserModelListener> listeners = new HashSet<BrowserModelListener>();
    private SimpleBrowserNode root = new SimpleBrowserNode("Limma");
    private BrowserModelNode currentNode = root;
    private BrowserListModel leftListModel;
    private BrowserListModel rightListModel;
    private ListSelectionModel leftListSelectionModel;
    private ListSelectionModel rightListSelectionModel;

    public BrowserModel() {
        leftListModel = new BrowserListModel(this, 0);
        rightListModel = new BrowserListModel(this, 1);

        leftListSelectionModel = new BrowserListSelectionModel(this, 0);
        rightListSelectionModel = new BrowserListSelectionModel(this, 1);

        leftListSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (leftListSelectionModel.isSelectedIndex(e.getFirstIndex())) {
                    updateRightModels(e.getFirstIndex());
                } else {
                    updateRightModels(e.getLastIndex());

                }
            }

            private void updateRightModels(int leftSelectedIndex) {
                rightListModel.fireContentsChanged();
                BrowserModelNode selectedLeftNode = getBaseNode().getChildren().get(leftSelectedIndex);
                int rightSelectionIndex = selectedLeftNode.getSelectedChildIndex();
                if (selectedLeftNode.getSelectedChild() != null) {
                    rightListSelectionModel.setSelectionInterval(rightSelectionIndex, rightSelectionIndex);
                } else {
                    rightListSelectionModel.clearSelection();
                }
            }
        });
    }

    public void add(MenuItem menuItem) {
        root.add(menuItem);
    }

    public void add(BrowserModelNode node) {
        root.add(node);
    }

    public void setBaseNode(BrowserModelNode newNode) {
        BrowserModelNode oldNode = this.currentNode;
        this.currentNode = newNode;

        fireCurrentNodeChanged(oldNode, newNode);
    }

    public BrowserModelNode getBaseNode() {
        return currentNode;
    }

    public void addListener(BrowserModelListener listener) {
        listeners.add(listener);
    }

    private void fireCurrentNodeChanged(BrowserModelNode oldNode, BrowserModelNode newNode) {
        for (BrowserModelListener listener : listeners) {
            listener.currentNodeChanged(this, oldNode, newNode);
        }
    }

    public ListModel getLeftListModel() {
        return leftListModel;
    }

    public ListModel getRightListModel() {
        return rightListModel;
    }

    public ListSelectionModel getLeftListSelectionModel() {
        return leftListSelectionModel;
    }

    public ListSelectionModel getRightListSelectionModel() {
        return rightListSelectionModel;
    }
}
