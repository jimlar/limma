package limma.swing.menu;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class MenuKeyListener extends KeyAdapter {
    private LimmaMenu list;
    private final MenuModel model;

    public MenuKeyListener(LimmaMenu list, MenuModel model) {
        this.list = list;
        this.model = model;
    }

    public void keyPressed(KeyEvent e) {
        MenuNode currentNode = model.getCurrentNode();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            MenuNode child = currentNode.getSelectedChild();
            if (!child.getChildren().isEmpty()) {
                model.setCurrentNode(child);
                list.scrollToSelected();
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            MenuNode parent = currentNode.getParent();
            if (parent != null) {
                model.setCurrentNode(parent);
                list.scrollToSelected();
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            MenuNode child = currentNode.getSelectedChild();
            child.performAction();
        }
    }
}
