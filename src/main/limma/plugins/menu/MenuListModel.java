package limma.plugins.menu;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListDataEvent;

import limma.swing.MenuNode;

public class MenuListModel implements ListModel {
    private MenuNode current;
    private List listeners = new ArrayList();

    public MenuListModel(MenuNode root) {
        setCurrent(root);
    }

    public MenuNode getCurrent() {
        return current;
    }

    public void setCurrent(MenuNode current) {
        this.current = current;
        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ListDataListener listener = (ListDataListener) i.next();
            listener.contentsChanged(new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, getSize()));
        }
    }

    public int getSize() {
        return current.getChildren().size();
    }

    public Object getElementAt(int index) {
        return current.getChildren().get(index);
    }

    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
