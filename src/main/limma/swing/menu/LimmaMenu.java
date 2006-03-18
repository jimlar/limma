package limma.swing.menu;

import limma.UIProperties;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

public class LimmaMenu extends JList {
    private List<MenuCellRenderer> renderers = new ArrayList<MenuCellRenderer>();
    private Set<MenuListener> listeners = new HashSet<MenuListener>();

    public LimmaMenu(final MenuModel model, UIProperties uiProperties) {
        super(model);
        addKeyListener(new MenuKeyListener(this, model));
        setSelectionModel(new MenuSelectionModelAdapter(model));
        setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                for (Iterator<MenuCellRenderer> i = renderers.iterator(); i.hasNext();) {
                    MenuCellRenderer renderer = i.next();
                    if (renderer.supportsRendering(value)) {
                        return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    }
                }
                throw new IllegalArgumentException("Rendering of " + value + " is not supported (is it a subclass of NavigationNode?)");
            }
        });
        setOpaque(false);
        addCellRenderer(new DefaultMenuCellRenderer(uiProperties));
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

    public void addCellRenderer(MenuCellRenderer renderer) {
        renderers.add(0, renderer);
    }

    public void addMenuListener(MenuListener listener) {
        listeners.add(listener);
    }

    public MenuModel getMenuModel() {
        return (MenuModel) getModel();
    }

    protected void fireFocusChanged() {
        MenuNode node = (MenuNode) getMenuModel().getElementAt(getSelectedIndex());
        for (Iterator<MenuListener> i = listeners.iterator(); i.hasNext();) {
            MenuListener listener = i.next();
            listener.menuItemFocusChanged(this, node);
        }
    }
}
