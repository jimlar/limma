package limma.swing.navigation;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;

import limma.Command;
import limma.UIProperties;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;

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


    public boolean consume(Command command) {
        switch (command) {
            case EXIT:
            case MENU:
                close();
                return true;

            case UP:
                if (list.getSelectedIndex() > 0) {
                    list.setSelectedIndex(list.getSelectedIndex() - 1);
                }
                return true;

            case DOWN:
                if (list.getSelectedIndex() < listModel.getSize() - 1) {
                    list.setSelectedIndex(list.getSelectedIndex() + 1);
                }
                return true;

            case ACTION:
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
