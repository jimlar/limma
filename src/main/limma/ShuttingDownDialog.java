package limma;

import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;

public class ShuttingDownDialog extends LimmaDialog {
    public ShuttingDownDialog(DialogManager dialogManager, UIProperties uiProperties) {
        super(dialogManager);
        add(new AntialiasLabel("Shutting down...", uiProperties));
    }


    public boolean consume(Command command) {
        return false;
    }
}
