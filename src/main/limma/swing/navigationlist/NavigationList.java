package limma.swing.navigationlist;

import javax.swing.*;
import java.awt.*;

public class NavigationList extends JComponent {

    public NavigationList(final NavigationModel model) {
        setLayout(new BorderLayout());
        JList list = new JList(model);
        list.addKeyListener(new NavigationListKeyListener(model));
        list.setSelectionModel(new ListSelectionModelAdapter(model));
        list.setCellRenderer(new NavigationListCellRenderer());
        add(list, BorderLayout.CENTER);
    }
}
