package limma.swing;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class NavigationList extends JComponent {

    public NavigationList(final NavigationModel model) {
        setLayout(new BorderLayout());
        final JList list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        add(list, BorderLayout.CENTER);
        list.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (list.getSelectedValue() != null) {
                        model.setCurrentNode((DefaultNavigationNode) list.getSelectedValue());
                        e.consume();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    NavigationNode parent = model.getCurrentNode().getParent();
                    if (parent != null) {
                        model.setCurrentNode(parent);
                        e.consume();
                    }
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });

        list.setSelectionModel(new ListSelectionModelAdapter(model));
    }

    private static class ListSelectionModelAdapter implements ListSelectionModel {
        private NavigationModel model;

        public ListSelectionModelAdapter(NavigationModel model) {
            this.model = model;
        }

        public void setSelectionInterval(int index0, int index1) {
        }

        public void addSelectionInterval(int index0, int index1) {
        }

        public void removeSelectionInterval(int index0, int index1) {
        }

        public int getMinSelectionIndex() {
            return 0;
        }

        public int getMaxSelectionIndex() {
            return 0;
        }

        public boolean isSelectedIndex(int index) {
            return false;
        }

        public int getAnchorSelectionIndex() {
            return 0;
        }

        public void setAnchorSelectionIndex(int index) {
        }

        public int getLeadSelectionIndex() {
            return 0;
        }

        public void setLeadSelectionIndex(int index) {
        }

        public void clearSelection() {
        }

        public boolean isSelectionEmpty() {
            return false;
        }

        public void insertIndexInterval(int index, int length, boolean before) {
        }

        public void removeIndexInterval(int index0, int index1) {
        }

        public void setValueIsAdjusting(boolean valueIsAdjusting) {
        }

        public boolean getValueIsAdjusting() {
            return false;
        }

        public void setSelectionMode(int selectionMode) {
        }

        public int getSelectionMode() {
            return ListSelectionModel.SINGLE_SELECTION;
        }

        public void addListSelectionListener(ListSelectionListener x) {
        }

        public void removeListSelectionListener(ListSelectionListener x) {
        }
    }
}
