package limma.ui.browser;

import limma.ui.dialogs.DialogManager;

public abstract class MenuItem {
    private String title;

    public MenuItem(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public abstract void performAction(DialogManager dialogManager);

    public String toString() {
        return getTitle();
    }
}
