package limma.swing.navigationlist;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class NavigationListKeyListener extends KeyAdapter {
    private final NavigationModel model;

    public NavigationListKeyListener(NavigationModel model) {
        this.model = model;
    }

    public void keyPressed(KeyEvent e) {
        NavigationNode currentNode = model.getCurrentNode();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            NavigationNode child = (NavigationNode) currentNode.getChildAt(currentNode.getSelectedChildIndex());
            if (child.getChildCount() > 0) {
                model.setCurrentNode(child);
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            NavigationNode parent = currentNode.getParent();
            if (parent != null) {
                model.setCurrentNode(parent);
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}
