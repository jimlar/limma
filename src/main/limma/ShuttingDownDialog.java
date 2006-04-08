package limma;

import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;

import java.awt.event.KeyEvent;

public class ShuttingDownDialog extends LimmaDialog {
    public ShuttingDownDialog(DialogManager dialogManager, UIProperties uiProperties) {
        super(dialogManager);
        add(new AntialiasLabel("Shutting down...", uiProperties));
    }

    public boolean keyPressed(KeyEvent e) {
        return false;
    }
}
