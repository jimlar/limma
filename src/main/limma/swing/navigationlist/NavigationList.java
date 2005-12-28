package limma.swing.navigationlist;

import javax.swing.*;
import java.awt.*;

public class NavigationList extends JComponent {
    private JList list;

    public NavigationList(final NavigationModel model) {
        setLayout(new BorderLayout());
        list = new JList(model);
        list.addKeyListener(new NavigationListKeyListener(this, model));
        list.setSelectionModel(new ListSelectionModelAdapter(model));
        list.setCellRenderer(new NavigationListCellRenderer());
        add(list, BorderLayout.CENTER);
        setOpaque(false);
        list.setOpaque(false);
    }

    public void scrollToSelected() {
        list.scrollRectToVisible(list.getCellBounds(list.getSelectedIndex(), list.getSelectedIndex()));
    }
}
