package limma.swing.navigationlist;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.util.*;
import java.util.List;

public class NavigationList extends JList {
    private List<NavigationListCellRenderer> renderers = new ArrayList<NavigationListCellRenderer>();

    public NavigationList(final NavigationModel model) {
        super(model);
        addKeyListener(new NavigationListKeyListener(this, model));
        setSelectionModel(new ListSelectionModelAdapter(model));
        setCellRenderer(new ListCellRenderer() {
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                for (Iterator<NavigationListCellRenderer> i = renderers.iterator(); i.hasNext();) {
                    NavigationListCellRenderer renderer = i.next();
                    if (renderer.supportsRendering(value)) {
                        return renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    }
                }
                return null;
            }
        });
        setOpaque(false);
        addCellRenderer(new DefaultNavigationListCellRenderer());
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

    public void addCellRenderer(NavigationListCellRenderer renderer) {
        renderers.add(0, renderer);
    }
}
