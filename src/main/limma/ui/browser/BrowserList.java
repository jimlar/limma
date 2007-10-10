package limma.ui.browser;

import limma.application.Command;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BrowserList extends JList {
    private List<NavigationNodeRenderer> renderers = new ArrayList<NavigationNodeRenderer>();
    private Set<NavigationListener> listeners = new HashSet<NavigationListener>();
    private DialogManager dialogManager;

    public BrowserList(final NavigationModel model, UIProperties uiProperties, DialogManager dialogManager) {
        super(model);
        this.dialogManager = dialogManager;

        setSelectionModel(new NavigationSelectionModelAdapter(model));
        setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                NavigationNode node = (NavigationNode) value;
                for (NavigationNodeRenderer renderer : renderers) {
                    if (renderer.supportsRendering(node)) {
                        return renderer.getNodeRendererComponent(BrowserList.this, node, index, isSelected & isEnabled(), cellHasFocus);
                    }
                }
                throw new IllegalArgumentException("Rendering of " + value + " is not supported (is it a subclass of NavigationNode?)");
            }
        });
        setOpaque(false);
        addCellRenderer(new DefaultNavigationNodeRenderer(uiProperties));
        addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                fireFocusChanged();
            }
        });
    }

    public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
        return -1;
    }

    public void addCellRenderer(NavigationNodeRenderer renderer) {
        renderers.add(0, renderer);
    }

    public void addNavigationListener(NavigationListener listener) {
        listeners.add(listener);
    }

    public NavigationModel getNavigationModel() {
        return (NavigationModel) getModel();
    }

    public boolean consume(Command command) {
        NavigationModel model = (NavigationModel) getModel();
        NavigationNode currentNode = model.getCurrentNode();
        NavigationNode child = currentNode.getSelectedChild();

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
                child.performAction(dialogManager);
                return true;

        }
        return false;
    }

    private void goDown() {
        if (getSelectedIndex() < getModel().getSize() - 1) {
            setSelectedIndex(getSelectedIndex() + 1);
            scrollToSelected();
        }
    }

    private void goUp() {
        if (getSelectedIndex() > 0) {
            setSelectedIndex(getSelectedIndex() - 1);
            scrollToSelected();
        }
    }

    private void goRight() {
        NavigationModel model = (NavigationModel) getModel();
        NavigationNode currentNode = model.getCurrentNode();
        NavigationNode child = currentNode.getSelectedChild();

        if (!child.getChildren().isEmpty()) {
            model.setCurrentNode(child);
            scrollToSelected();
            fireFocusChanged();
        }
    }

    private void goLeft() {
        NavigationModel model = (NavigationModel) getModel();
        NavigationNode currentNode = model.getCurrentNode();

        NavigationNode parent = currentNode.getParent();
        if (parent != null) {
            model.setCurrentNode(parent);
            scrollToSelected();
            fireFocusChanged();
        }
    }

    private void openMenu() {
        NavigationModel model = (NavigationModel) getModel();
        NavigationNode currentNode = model.getCurrentNode();
        NavigationNode child = currentNode.getSelectedChild();

        List<MenuItem> menuItems = child.getAllMenuItems();
        if (!menuItems.isEmpty()) {
            NavigationPopupMenu menu = (NavigationPopupMenu) dialogManager.createAndOpen(NavigationPopupMenu.class);
            menu.setItems(menuItems);
        }
    }

    private void fireFocusChanged() {
        for (NavigationListener listener : listeners) {
            listener.navigationNodeFocusChanged();
        }
    }

    private void scrollToSelected() {
        scrollRectToVisible(getCellBounds(getSelectedIndex(), getSelectedIndex()));
    }
}
