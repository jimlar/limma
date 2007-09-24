package limma.swing.navigation;

import java.awt.Component;
import java.util.*;

import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;

import limma.Command;
import limma.CommandConsumer;
import limma.UIProperties;
import limma.swing.DialogManager;

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
                List<MenuItem> menuItems = child.getAllMenuItems();
                if (!menuItems.isEmpty()) {
                    NavigationPopupMenu menu = (NavigationPopupMenu) dialogManager.createAndOpen(NavigationPopupMenu.class);
                    menu.setItems(menuItems);
                }
                return true;

            case LEFT:
                NavigationNode parent = currentNode.getParent();
                if (parent != null) {
                    model.setCurrentNode(parent);
                    scrollToSelected();
                    fireFocusChanged();
                }
                return true;

            case RIGHT:
                if (!child.getChildren().isEmpty()) {
                    model.setCurrentNode(child);
                    scrollToSelected();
                    fireFocusChanged();
                }
                return true;

            case UP:
                if (getSelectedIndex() > 0) {
                    setSelectedIndex(getSelectedIndex() - 1);
                    scrollToSelected();
                }
                return true;

            case DOWN:
                if (getSelectedIndex() < getModel().getSize() - 1) {
                    setSelectedIndex(getSelectedIndex() + 1);
                    scrollToSelected();
                }
                return true;

            case ACTION:
                child.performAction(dialogManager);
                return true;

        }
        return false;
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
