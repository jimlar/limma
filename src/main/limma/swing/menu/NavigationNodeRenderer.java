package limma.swing.menu;

import java.awt.*;

public interface NavigationNodeRenderer {
    boolean supportsRendering(NavigationNode value);

    Component getNodeRendererComponent(Navigation navigation, NavigationNode value, int index, boolean isSelected, boolean cellHasFocus);
}
