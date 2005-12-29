package limma.swing.navigationlist;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

class NavigationListKeyListener extends KeyAdapter {
    private NavigationList list;
    private final NavigationModel model;

    public NavigationListKeyListener(NavigationList list, NavigationModel model) {
        this.list = list;
        this.model = model;
    }

    public void keyPressed(KeyEvent e) {
        NavigationNode currentNode = model.getCurrentNode();
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            NavigationNode child = (NavigationNode) currentNode.getChildAt(currentNode.getSelectedChildIndex());
            if (child.getChildCount() > 0) {
                model.setCurrentNode(child);
                list.scrollToSelected();
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            NavigationNode parent = currentNode.getParent();
            if (parent != null) {
                model.setCurrentNode(parent);
                list.scrollToSelected();
                e.consume();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            NavigationNode child = (NavigationNode) currentNode.getChildAt(currentNode.getSelectedChildIndex());
            child.performAction();
            
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }
    }
}
