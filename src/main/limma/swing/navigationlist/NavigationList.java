package limma.swing.navigationlist;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.event.KeyEvent;

public class NavigationList extends JList {

    public NavigationList(final NavigationModel model) {
        super(model);
        addKeyListener(new NavigationListKeyListener(this, model));
        setSelectionModel(new ListSelectionModelAdapter(model));
        setCellRenderer(new NavigationListCellRenderer());
        setOpaque(false);
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
}
