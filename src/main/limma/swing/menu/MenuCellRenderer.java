package limma.swing.menu;

import javax.swing.*;

public interface MenuCellRenderer extends ListCellRenderer {
    boolean supportsRendering(Object value);
}
