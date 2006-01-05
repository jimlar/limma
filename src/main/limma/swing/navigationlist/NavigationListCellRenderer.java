package limma.swing.navigationlist;

import javax.swing.*;

public interface NavigationListCellRenderer extends ListCellRenderer {
    boolean supportsRendering(Object value);
}
