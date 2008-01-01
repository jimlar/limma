package limma.ui.browser;

import limma.ui.browser.model.BrowserModelNode;

import java.awt.*;

public interface BrowserNodeRenderer {
    boolean supportsRendering(BrowserModelNode value);

    Component getNodeRendererComponent(BrowserList browserList, BrowserModelNode value, int index, boolean isSelected, boolean cellHasFocus);
}
