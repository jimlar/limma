package limma.swing.navigation;

import limma.swing.DialogManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

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
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            NavigationNode child = currentNode.getSelectedChild();
            if (!child.getChildren().isEmpty()) {
                model.setCurrentNode(child);
                list.scrollToSelected();
                list.fireFocusChanged();
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            NavigationNode parent = currentNode.getParent();
            if (parent != null) {
                model.setCurrentNode(parent);
                list.scrollToSelected();
                list.fireFocusChanged();
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            NavigationNode child = currentNode.getSelectedChild();
            child.performAction(dialogManager);

        }
    }
}
