package limma.ui.browser.model;

public interface BrowserModelListener {
    void currentNodeChanged(BrowserModel browserModel, BrowserModelNode oldNode, BrowserModelNode newNode);
}
