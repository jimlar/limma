package limma.swing.menu;

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

    public Navigation(final NavigationModel model, UIProperties uiProperties, DialogManager dialogManager) {
        super(model);
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
        super.processKeyEvent(e);
    }

    public void addCellRenderer(NavigationNodeRenderer renderer) {
        renderers.add(0, renderer);
    }

    public void addMenuListener(NavigationListener listener) {
        listeners.add(listener);
    }

    public NavigationModel getMenuModel() {
        return (NavigationModel) getModel();
    }

    protected void fireFocusChanged() {
        NavigationNode node = (NavigationNode) getMenuModel().getElementAt(getSelectedIndex());
        for (Iterator<NavigationListener> i = listeners.iterator(); i.hasNext();) {
            NavigationListener listener = i.next();
            listener.navigationNodeFocusChanged(this, node);
        }
    }
}
