package limma.swing.navigationlist;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.*;
import java.awt.event.KeyEvent;

public class NavigationList extends JComponent {
    private MyJList list;

    public NavigationList(final NavigationModel model) {
        setLayout(new BorderLayout());
        list = new MyJList(model);
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

    public void processKeyEvent(KeyEvent e) {
        list.processKeyEvent(e);
    }

    private class MyJList extends JList {
        public MyJList(NavigationModel model) {
            super(model);
        }

        public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
            return -1;
        }

        public void processKeyEvent(KeyEvent e) {
            super.processKeyEvent(e);
        };
    }
}
