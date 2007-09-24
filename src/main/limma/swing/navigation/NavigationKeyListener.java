package limma.swing.navigation;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import limma.swing.DialogManager;

class NavigationKeyListener extends KeyAdapter {
    private Navigation list;
    private final NavigationModel model;
    private DialogManager dialogManager;

    public NavigationKeyListener(Navigation list, NavigationModel model, DialogManager dialogManager) {
        this.list = list;
        this.model = model;
        this.dialogManager = dialogManager;
    }

    public void keyPressed(KeyEvent e) {
        NavigationNode currentNode = model.getCurrentNode();
        NavigationNode child = currentNode.getSelectedChild();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                if (!child.getChildren().isEmpty()) {
                    model.setCurrentNode(child);
                    list.scrollToSelected();
                    list.fireFocusChanged();
                    e.consume();
                }
                break;

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                NavigationNode parent = currentNode.getParent();
                if (parent != null) {
                    model.setCurrentNode(parent);
                    list.scrollToSelected();
                    list.fireFocusChanged();
                    e.consume();
                }
                break;
            case KeyEvent.VK_ENTER:
                child.performAction(dialogManager);
                break;
        }
    }
}
