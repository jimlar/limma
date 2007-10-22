package limma.ui.browser;

import limma.application.Command;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class BrowserImpl extends JPanel implements Browser {
    private BrowserList leftList;
    private BrowserList rightList;
    private BrowserList activeList;
    private NavigationModel navigationModel;
    private DialogManager dialogManager;
    private Set<NavigationListener> listeners = new HashSet<NavigationListener>();

    public BrowserImpl(final NavigationModel model, UIProperties uiProperties, DialogManager dialogManager) {
        this.navigationModel = model;
        this.dialogManager = dialogManager;


        final RightBrowserListModel rightBrowserListModel = new RightBrowserListModel(model);


        leftList = createList(uiProperties, model);
        rightList = createList(uiProperties, rightBrowserListModel);


        leftList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                rightList.setModel(rightBrowserListModel);
            }
        });


        activateLeftList();
        setOpaque(false);

        setLayout(new GridBagLayout());
        add(wrapInScrollPane(leftList), new GridBagConstraints(0, 0, 1, 1, 0.3, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(wrapInScrollPane(rightList), new GridBagConstraints(1, 0, 1, 1, 0.7, 1, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    private Component wrapInScrollPane(BrowserList list) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        return scrollPane;
    }

    private BrowserList createList(UIProperties uiProperties, ListModel model) {
        BrowserList list = new BrowserList(uiProperties, model);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                selectedNodeChanged();
            }
        });
        return list;
    }

    public void addCellRenderer(NavigationNodeRenderer renderer) {
        leftList.addCellRenderer(renderer);
        rightList.addCellRenderer(renderer);
    }

    public void addNavigationListener(NavigationListener listener) {
        listeners.add(listener);
    }

    public NavigationNode getSelectedNode() {
        if (isLeftListActive()) {
            return navigationModel.getCurrentNode().getSelectedChild();
        } else {
            return navigationModel.getCurrentNode().getSelectedChild().getSelectedChild();
        }
    }

    public boolean consume(Command command) {
        switch (command) {
            case MENU:
                openMenu();
                return true;

            case LEFT:
                goLeft();
                return true;

            case RIGHT:
                goRight();
                return true;

            case UP:
                goUp();
                return true;

            case DOWN:
                goDown();
                return true;

            case ACTION:
                getSelectedNode().performAction(dialogManager);
                return true;

        }
        return false;
    }

    private void activateRightList() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                rightList.setEnabled(true);
                leftList.setEnabled(false);
                activeList = rightList;
                repaint();
            }
        });
    }

    private boolean isLeftListActive() {
        return activeList == leftList;
    }

    private void activateLeftList() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                rightList.setEnabled(false);
                leftList.setEnabled(true);
                activeList = leftList;
                repaint();
            }
        });
    }

    private void goDown() {
        if (activeList.getSelectedIndex() < activeList.getModel().getSize() - 1) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    activeList.setSelectedIndex(activeList.getSelectedIndex() + 1);
                    repaint();
                }
            });
        }
    }

    private void goUp() {
        if (activeList.getSelectedIndex() > 0) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    activeList.setSelectedIndex(activeList.getSelectedIndex() - 1);
                    repaint();
                }
            });
        }
    }

    private void goLeft() {
        if (!isLeftListActive()) {
            activateLeftList();
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NavigationModel model = navigationModel;
                NavigationNode currentNode = model.getCurrentNode();

                NavigationNode parent = currentNode.getParent();
                if (parent != null) {
                    model.setCurrentNode(parent);
                    activeList.setSelectedIndex(parent.getSelectedChildIndex());
                    selectedNodeChanged();
                }
                repaint();
            }
        });
    }

    private void goRight() {
        if (isLeftListActive()) {
            rightList.setSelectedIndex(getSelectedNode().getSelectedChildIndex());
            activateRightList();
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NavigationNode selectedNode = getSelectedNode();

                if (!selectedNode.getChildren().isEmpty()) {
                    navigationModel.setCurrentNode(selectedNode.getParent());
                    leftList.setSelectedIndex(selectedNode.getParent().getSelectedChildIndex());
                    rightList.setSelectedIndex(selectedNode.getSelectedChildIndex());
                    selectedNodeChanged();
                }
                repaint();
            }
        });
    }

    private void openMenu() {
        java.util.List<MenuItem> menuItems = getSelectedNode().getAllMenuItems();
        if (!menuItems.isEmpty()) {
            NavigationPopupMenu menu = (NavigationPopupMenu) dialogManager.createAndOpen(NavigationPopupMenu.class);
            menu.setItems(menuItems);
        }
    }

    protected void selectedNodeChanged() {
        for (NavigationListener listener : listeners) {
            listener.navigationNodeFocusChanged();
        }
    }
}
