package limma.ui.browser;

import java.awt.Component;

public interface NavigationNodeRenderer {
    boolean supportsRendering(NavigationNode value);

    Component getNodeRendererComponent(Navigation navigation, NavigationNode value, int index, boolean isSelected, boolean cellHasFocus);
}
