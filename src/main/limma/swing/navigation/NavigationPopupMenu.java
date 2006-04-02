package limma.swing.navigation;

import limma.UIProperties;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.List;

public class NavigationPopupMenu extends LimmaDialog {
    private JList list;
    private DefaultListModel listModel;

    public NavigationPopupMenu(DialogManager dialogManager, UIProperties uiProperties) {
        super(dialogManager);
        listModel = new DefaultListModel();
        list = new JList(listModel);
        DefaultListCellRenderer cellRenderer = new DefaultListCellRenderer();
        list.setFont(uiProperties.getLargeFont());
        list.setCellRenderer(cellRenderer);
        list.setOpaque(false);
        list.setSelectionForeground(Color.white);
        list.setSelectionBackground(Color.blue);
        list.setForeground(Color.white);
        list.setBackground(Color.black);
        add(list);
    }

    public boolean keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                close();
                return true;
            case KeyEvent.VK_UP:
                if (list.getSelectedIndex() > 0) {
                    list.setSelectedIndex(list.getSelectedIndex() - 1);
                }
                return true;
            case KeyEvent.VK_DOWN:
                if (list.getSelectedIndex() < listModel.getSize() - 1) {
                    list.setSelectedIndex(list.getSelectedIndex() + 1);
                }
                return true;
            case KeyEvent.VK_ENTER:
                MenuItem menuItem = (MenuItem) list.getSelectedValue();
                close();
                menuItem.performAction(getDialogManager());
                return true;
        }
        return false;
    }

    public void setItems(List<MenuItem> menuItems) {
        listModel.removeAllElements();
        for (Iterator<MenuItem> i = menuItems.iterator(); i.hasNext();) {
            MenuItem menuItem = i.next();
            listModel.addElement(menuItem);
        }
        list.setSelectedIndex(0);
        invalidate();
        doLayout();
    }
}
