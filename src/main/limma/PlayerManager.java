package limma;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import limma.ui.Player;

public class PlayerManager {
    private Player player;
    private List<PlayerManagerListener> listeners = new ArrayList<PlayerManagerListener>();

    public void switchTo(Player player) {
        if (this.player != null) {
            this.player.consume(Command.STOP);
        }
        this.player = player;
        firePlayerSwitched();
    }

    public Player getPlayer() {
        return player;
    }

    public void addListener(PlayerManagerListener listener) {
        listeners.add(listener);
    }

    private void firePlayerSwitched() {
        for (Iterator<PlayerManagerListener> i = listeners.iterator(); i.hasNext();) {
            PlayerManagerListener listener = i.next();
            listener.playerSwitched(player);
        }
    }
}
