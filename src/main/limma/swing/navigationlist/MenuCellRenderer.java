package limma.swing.navigationlist;

import javax.swing.*;

public interface MenuCellRenderer extends ListCellRenderer {
    boolean supportsRendering(Object value);
}
