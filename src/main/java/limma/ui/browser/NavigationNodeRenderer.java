package limma.ui.browser;

import java.awt.*;

public interface NavigationNodeRenderer {
    boolean supportsRendering(NavigationNode value);

    Component getNodeRendererComponent(BrowserList browserList, NavigationNode value, int index, boolean isSelected, boolean cellHasFocus);
}
