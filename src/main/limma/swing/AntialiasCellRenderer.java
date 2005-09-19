package limma.swing;

import javax.swing.*;
import java.awt.*;

public class AntialiasCellRenderer implements ListCellRenderer {
    private static final Color SELECTED_BACKGROUND = Color.blue.darker().darker();

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        AntialiasLabel antialiasLabel = new AntialiasLabel(value.toString());
        antialiasLabel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        if (isSelected) {
            antialiasLabel.setBackground(SELECTED_BACKGROUND);
            antialiasLabel.setOpaque(true);
        }
        return antialiasLabel;
    }

}
