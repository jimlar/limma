package limma.swing;

import javax.swing.*;
import java.awt.*;

public class AntialiasCellRenderer extends AntialiasLabel implements ListCellRenderer {
    private static final Color SELECTED_BACKGROUND = Color.blue.darker().darker();

    public AntialiasCellRenderer() {
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(String.valueOf(value));

        setComponentOrientation(list.getComponentOrientation());
        setForeground(Color.white);
        setBackground(isSelected ? SELECTED_BACKGROUND : list.getBackground());
        setOpaque(isSelected);
        return this;
    }
}
