package limma.swing;

import java.awt.event.KeyEvent;

public class MenuDialog extends LimmaDialog {
    private LimmaMenu limmaMenu;

    public MenuDialog(DialogManager dialogManager) {
        super(dialogManager);
        limmaMenu = new LimmaMenu(new Runnable() {
            public void run() {
                close();
            }
        });
        add(limmaMenu);
    }

    public void addItem(final LimmaMenuItem menuItem) {
        limmaMenu.add(menuItem);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            close();
        }
        limmaMenu.keyPressed(e);
    }

    public void open() {
        super.open();
        limmaMenu.select(0);
    }
}
