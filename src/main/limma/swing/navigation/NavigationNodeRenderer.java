package limma.swing.navigation;

import java.awt.*;

public interface NavigationNodeRenderer {
    boolean supportsRendering(NavigationNode value);

    Component getNodeRendererComponent(Navigation navigation, NavigationNode value, int index, boolean isSelected, boolean cellHasFocus);
}
