package limma.ui;

import limma.application.Command;
import limma.application.PlayerManager;
import limma.ui.browser.Browser;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.LimmaDialog;
import org.picocontainer.Startable;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CommandDispatcher extends EventQueue implements Startable {
    private KeyConfig keyConfig;
    private DialogManager dialogManager;
    private Browser browser;
    private PlayerManager playerManager;

    public CommandDispatcher(KeyConfig keyConfig, DialogManager dialogManager, Browser browser, PlayerManager playerManager) {
        this.keyConfig = keyConfig;
        this.dialogManager = dialogManager;
        this.browser = browser;
        this.playerManager = playerManager;
    }

    public void start() {
        Toolkit.getDefaultToolkit().getSystemEventQueue().push(this);
    }

    public void stop() {
    }

    protected void dispatchEvent(AWTEvent event) {
        if (event instanceof KeyEvent && event.getID() == KeyEvent.KEY_PRESSED) {

            KeyEvent keyEvent = (KeyEvent) event;
            Command command = keyConfig.getCommandForEvent(keyEvent);

            LimmaDialog topDialog = dialogManager.getTopDialog();
            if (topDialog != null) {
                if (topDialog.consume(command)) {
                    return;
                }

            } else {
                Player player = playerManager.getPlayer();
                if (player == null || !player.consume(command)) {
                    browser.consume(command);
                }
                return;
            }
        }
        super.dispatchEvent(event);
    }
}
