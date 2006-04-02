package limma.swing.navigation;

import limma.UIProperties;
import limma.swing.DialogManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class Navigation extends JList {
    private List<NavigationNodeRenderer> renderers = new ArrayList<NavigationNodeRenderer>();
    private Set<NavigationListener> listeners = new HashSet<NavigationListener>();
    private DialogManager dialogManager;

    public Navigation(final NavigationModel model, UIProperties uiProperties, DialogManager dialogManager) {
        super(model);
        this.dialogManager = dialogManager;

        addKeyListener(new NavigationKeyListener(this, model, dialogManager));
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

    public void processKeyEvent(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_M:
                NavigationNode currentNode = getNavigationModel().getCurrentNode();
                NavigationNode child = currentNode.getSelectedChild();
                List<MenuItem> menuItems = child.getMenuItems();
                if (!menuItems.isEmpty()) {
                    NavigationPopupMenu menu = (NavigationPopupMenu) dialogManager.createAndOpen(NavigationPopupMenu.class);
                    menu.setItems(menuItems);
                }
                break;
            default:
                super.processKeyEvent(e);

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
