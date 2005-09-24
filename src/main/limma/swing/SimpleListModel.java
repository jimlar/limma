package limma.swing;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

public class SimpleListModel extends AbstractListModel {
    private List objects = Collections.EMPTY_LIST;

    public int getSize() {
        return objects.size();
    }

    public Object getElementAt(int index) {
        return objects.get(index);
    }

    public void setObjects(List objects) {
        List oldObjects = this.objects;
        this.objects = objects;
        if (oldObjects.size() > 0) {
            fireIntervalRemoved(this, 0, oldObjects.size() - 1);
        }
        if (objects.size() > 0) {
            fireIntervalAdded(this, 0, objects.size() - 1);
        }
    }

    public int indexOf(Object o) {
        return objects.indexOf(o);
    }

    public void fireChanged(Object o) {
        int i = indexOf(o);
        if (i != -1) {
            fireContentsChanged(this, i, i);
        }
    }
}
