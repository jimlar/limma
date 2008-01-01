package limma.ui.browser;

import limma.application.Command;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.LimmaDialog;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class BrowserPopupMenu extends LimmaDialog {
    private JList list;
    private DefaultListModel listModel;

    public BrowserPopupMenu(DialogManager dialogManager, UIProperties uiProperties) {
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
                limma.ui.browser.model.MenuItem menuItem = (limma.ui.browser.model.MenuItem) list.getSelectedValue();
                close();
                menuItem.performAction(getDialogManager());
                return true;

        }
        return false;
    }

    public void setItems(List<limma.ui.browser.model.MenuItem> menuItems) {
        listModel.removeAllElements();
        for (Iterator<limma.ui.browser.model.MenuItem> i = menuItems.iterator(); i.hasNext();) {
            limma.ui.browser.model.MenuItem menuItem = i.next();
            listModel.addElement(menuItem);
        }
        list.setSelectedIndex(0);
        invalidate();
        doLayout();
    }
}
