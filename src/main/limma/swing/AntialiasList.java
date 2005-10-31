package limma.swing;

import javax.swing.*;
import javax.swing.text.Position;
import java.awt.event.KeyEvent;

public class AntialiasList extends JList {
    public AntialiasList(ListModel model) {
        super(model);
        setCellRenderer(new AntialiasCellRenderer());
        setOpaque(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public int getNextMatch(String prefix, int startIndex, Position.Bias bias) {
        return -1;
    }

    public void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }
}
