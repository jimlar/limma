package limma.swing.navigationlist;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

class ListSelectionModelAdapter implements ListSelectionModel {
    private NavigationModel model;
    private List<ListSelectionListener> listeners = new ArrayList<ListSelectionListener>();

    public ListSelectionModelAdapter(NavigationModel model) {
        this.model = model;
    }

    public void setSelectionInterval(int index0, int index1) {
        int oldSelected = model.getCurrentNode().getSelectedChildIndex();

        if (oldSelected != index0) {
            model.getCurrentNode().setSelectedChildIndex(index0);
            for (Iterator<ListSelectionListener> i = listeners.iterator(); i.hasNext();) {
                ListSelectionListener listener = i.next();
                listener.valueChanged(new ListSelectionEvent(this, oldSelected, oldSelected, getValueIsAdjusting()));
                listener.valueChanged(new ListSelectionEvent(this, index0, index0, getValueIsAdjusting()));
            }
        }
    }

    public void addSelectionInterval(int index0, int index1) {
        System.out.println("2");
    }

    public void removeSelectionInterval(int index0, int index1) {
        System.out.println("3");
    }

    public int getMinSelectionIndex() {
        return model.getCurrentNode().getSelectedChildIndex();
    }

    public int getMaxSelectionIndex() {
        return model.getCurrentNode().getSelectedChildIndex();
    }

    public boolean isSelectedIndex(int index) {
        return model.getCurrentNode().getSelectedChildIndex() == index;
    }

    public int getAnchorSelectionIndex() {
        return model.getCurrentNode().getSelectedChildIndex();
    }

    public void setAnchorSelectionIndex(int index) {
        System.out.println("4");
    }

    public int getLeadSelectionIndex() {
        return model.getCurrentNode().getSelectedChildIndex();
    }

    public void setLeadSelectionIndex(int index) {
        System.out.println("5");
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
        listeners.add(x);
    }

    public void removeListSelectionListener(ListSelectionListener x) {
        listeners.remove(x);
    }
}
