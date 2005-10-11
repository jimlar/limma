package limma.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class LimmaMenu extends JPanel {
    private DefaultListModel listModel;
    private AntialiasList list;
    private Runnable exitAction;

    public LimmaMenu(Runnable exitAction) {
        this.exitAction = exitAction;
        setOpaque(false);
        listModel = new DefaultListModel();
        list = new AntialiasList(listModel);
        list.setCellRenderer(new MenuCellRenderer());

        list.setSelectedIndex(0);

        setLayout(new GridBagLayout());
        add(list, new GridBagConstraints(0, 0, 1, 1, 1, 1, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    }

    public void select(int item) {
        list.setSelectedIndex(item);
    }

    public void add(LimmaMenuItem item) {
        listModel.addElement(item);
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                LimmaMenuItem selectedItem = (LimmaMenuItem) list.getSelectedValue();
                selectedItem.execute();
                break;
            case KeyEvent.VK_ESCAPE:
                exitAction.run();
                break;
            default:
                list.processKeyEvent(e);
        }
    }
}
