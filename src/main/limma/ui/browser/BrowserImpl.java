package limma.ui.browser;

import limma.application.Command;
import limma.ui.UIProperties;
import limma.ui.dialogs.DialogManager;

import javax.swing.*;
import java.awt.*;

public class BrowserImpl extends JPanel implements Browser {
    private BrowserList leftList;
    private BrowserList rightList;
    private BrowserList activeList;

    public BrowserImpl(final NavigationModel model, UIProperties uiProperties, DialogManager dialogManager) {
        leftList = new BrowserList(model, uiProperties, dialogManager);
        rightList = new BrowserList(model, uiProperties, dialogManager);
        activeList = leftList;
        leftList.setEnabled(true);
        rightList.setEnabled(false);

        setLayout(new GridLayout(1, 2));
        add(leftList);
        add(rightList);
    }

    public void addCellRenderer(NavigationNodeRenderer renderer) {
        leftList.addCellRenderer(renderer);
        rightList.addCellRenderer(renderer);
    }

    public void addNavigationListener(NavigationListener listener) {
        leftList.addNavigationListener(listener);
        rightList.addNavigationListener(listener);
    }

    public boolean consume(Command command) {

        if (command.equals(Command.LEFT) && activeList != leftList) {
            rightList.setEnabled(false);
            leftList.setEnabled(true);
            activeList = leftList;
            repaint();
            return true;
        }

        if (command.equals(Command.RIGHT) && activeList != rightList) {
            rightList.setEnabled(true);
            leftList.setEnabled(false);
            activeList = rightList;
            repaint();
            return true;
        }
        return activeList.consume(command);
    }
}
