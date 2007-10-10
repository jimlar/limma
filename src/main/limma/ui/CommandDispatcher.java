package limma.ui;

import limma.Command;
import limma.KeyConfig;
import limma.PlayerManager;
import limma.ui.browser.Navigation;
import limma.ui.dialogs.DialogManager;
import limma.ui.dialogs.LimmaDialog;
import org.picocontainer.Startable;

import java.awt.*;
import java.awt.event.KeyEvent;

public class CommandDispatcher extends EventQueue implements Startable {
    private KeyConfig keyConfig;
    private DialogManager dialogManager;
    private Navigation navigation;
    private PlayerManager playerManager;

    public CommandDispatcher(KeyConfig keyConfig, DialogManager dialogManager, Navigation navigation, PlayerManager playerManager) {
        this.keyConfig = keyConfig;
        this.dialogManager = dialogManager;
        this.navigation = navigation;
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

            System.out.println("Mapped key " + KeyEvent.getKeyText(keyEvent.getKeyCode()) + " to command " + command);

            LimmaDialog topDialog = dialogManager.getTopDialog();
            if (topDialog != null) {
                if (topDialog.consume(command)) {
                    return;
                }

            } else {
                Player player = playerManager.getPlayer();
                if (player == null || !player.consume(command)) {
                    navigation.consume(command);
                }
                return;
            }
        }
        super.dispatchEvent(event);
    }
}
