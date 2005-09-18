package limma.swing;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class AntialiasList extends JList {
    public AntialiasList(ListModel model) {
        super(model);
        setCellRenderer(new AntialiasCellRenderer());
        setOpaque(false);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    public void processKeyEvent(KeyEvent e) {
        super.processKeyEvent(e);
    }
}
