package limma.swing;

import java.awt.event.KeyEvent;

public class MenuDialog extends LimmaDialog {
    private MenuNode root;
    private LimmaMenu limmaMenu;

    public MenuDialog(DialogManager dialogManager) {
        super(dialogManager);
        root = new MenuNode("root") {
            public void execute() {
                close();
            }
        };
        limmaMenu = new LimmaMenu(root);
        add(limmaMenu);
    }

    public void add(final MenuNode menuNode) {
        root.add(menuNode);
    }

    public void keyPressed(KeyEvent e) {
        limmaMenu.keyPressed(e);
    }
}
