package limma.ui.browser;

import limma.application.Command;
import limma.ui.UIProperties;
import limma.ui.browser.model.BrowserModel;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.dialogs.DialogManager;
import net.miginfocom.swing.MigLayout;

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
    private BrowserModel browserModel;
    private DialogManager dialogManager;
    private Set<BrowserListener> listeners = new HashSet<BrowserListener>();
    private UIProperties uiProperties;

    public BrowserImpl(final BrowserModel model, UIProperties uiProperties, DialogManager dialogManager) {
        this.browserModel = model;
        this.dialogManager = dialogManager;
        this.uiProperties = uiProperties;


        leftList = createList(uiProperties, model.getLeftListModel(), model.getLeftListSelectionModel());
        rightList = createList(uiProperties, model.getRightListModel(), model.getRightListSelectionModel());

        activateLeftList();
        setOpaque(false);
        setLayout(new MigLayout("", "[200]5[ grow]", "[ grow]"));
        add(wrapInScrollPane(leftList, false), "grow");
        add(wrapInScrollPane(rightList, true), "grow");
        setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
    }

    private Component wrapInScrollPane(BrowserList list, boolean scrollbar) {
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, uiProperties.getBorderColor()));
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(scrollbar ? JScrollPane.VERTICAL_SCROLLBAR_ALWAYS : JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setOpaque(false);
        return scrollPane;
    }

    private BrowserList createList(UIProperties uiProperties, ListModel model, ListSelectionModel listSelectionModel) {
        BrowserList list = new BrowserList(uiProperties, model);
        list.setSelectionModel(listSelectionModel);
        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        selectedNodeChanged();
                    }
                });
            }
        });
        return list;
    }

    public void addCellRenderer(BrowserNodeRenderer renderer) {
        leftList.addCellRenderer(renderer);
        rightList.addCellRenderer(renderer);
    }

    public void addNavigationListener(BrowserListener listener) {
        listeners.add(listener);
    }

    public BrowserModelNode getSelectedNode() {
        if (isLeftListActive()) {
            return browserModel.getBaseNode().getSelectedChild();
        } else {
            return browserModel.getBaseNode().getSelectedChild().getSelectedChild();
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
            }
        });
    }

    private void goDown() {
        if (activeList.getSelectedIndex() < activeList.getModel().getSize() - 1) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    activeList.setSelectedIndex(activeList.getSelectedIndex() + 1);
                }
            });
        }
    }

    private void goUp() {
        if (activeList.getSelectedIndex() > 0) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    activeList.setSelectedIndex(activeList.getSelectedIndex() - 1);
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
                BrowserModel model = browserModel;
                BrowserModelNode currentNode = model.getBaseNode();

                BrowserModelNode parent = currentNode.getParent();
                if (parent != null) {
                    model.setBaseNode(parent);
                    activeList.setSelectedIndex(parent.getSelectedChildIndex());
                    selectedNodeChanged();
                }
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
                BrowserModelNode selectedNode = getSelectedNode();

                if (!selectedNode.getChildren().isEmpty()) {
                    browserModel.setBaseNode(selectedNode.getParent());
                    leftList.setSelectedIndex(selectedNode.getParent().getSelectedChildIndex());
                    rightList.setSelectedIndex(selectedNode.getSelectedChildIndex());
                    selectedNodeChanged();
                }
            }
        });
    }

    private void openMenu() {
        java.util.List<limma.ui.browser.model.MenuItem> menuItems = getSelectedNode().getAllMenuItems();
        if (!menuItems.isEmpty()) {
            BrowserPopupMenu menu = (BrowserPopupMenu) dialogManager.createAndOpen(BrowserPopupMenu.class);
            menu.setItems(menuItems);
        }
    }

    private void selectedNodeChanged() {
        for (BrowserListener listener : listeners) {
            listener.navigationNodeFocusChanged();
        }
    }
}
