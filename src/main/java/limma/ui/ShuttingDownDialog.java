package limma.ui;

import limma.application.Command;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.LimmaDialog;

public class ShuttingDownDialog extends LimmaDialog {
    public ShuttingDownDialog(DialogManager dialogManager, UIProperties uiProperties) {
        super(dialogManager);
        add(new AntialiasLabel("Shutting down...", uiProperties));
    }


    public boolean consume(Command command) {
        return false;
    }
}
