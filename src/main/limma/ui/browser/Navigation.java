package limma.ui.browser;

import java.awt.Component;
import java.util.*;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import limma.Command;
import limma.CommandConsumer;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;

public class Navigation extends JList implements CommandConsumer {
    private List<NavigationNodeRenderer> renderers = new ArrayList<NavigationNodeRenderer>();
    private Set<NavigationListener> listeners = new HashSet<NavigationListener>();
    private DialogManager dialogManager;

    public Navigation(final NavigationModel model, UIProperties uiProperties, DialogManager dialogManager) {
        super(model);
        this.dialogManager = dialogManager;

        setSelectionModel(new NavigationSelectionModelAdapter(model));
        setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                NavigationNode node = (NavigationNode) value;
                for (Iterator<NavigationNodeRenderer> i = renderers.iterator(); i.hasNext();) {
                    NavigationNodeRenderer renderer = i.next();
                    if (renderer.supportsRendering(node)) {
                        return renderer.getNodeRendererComponent(Navigation.this, node, index, isSelected, cellHasFocus);
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

    public void scrollToSelected() {
        scrollRectToVisible(getCellBounds(getSelectedIndex(), getSelectedIndex()));
    }

    public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
        return -1;
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

    public void addCellRenderer(NavigationNodeRenderer renderer) {
        renderers.add(0, renderer);
    }

    public void addNavigationListener(NavigationListener listener) {
        listeners.add(listener);
    }

    public NavigationModel getNavigationModel() {
        return (NavigationModel) getModel();
    }

    protected void fireFocusChanged() {
        NavigationNode node = (NavigationNode) getNavigationModel().getElementAt(getSelectedIndex());
        for (Iterator<NavigationListener> i = listeners.iterator(); i.hasNext();) {
            NavigationListener listener = i.next();
            listener.navigationNodeFocusChanged(this, node);
        }
    }
}
