package limma;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class PlayerManager {
    public Player player;
    private List<PlayerManagerListener> listeners = new ArrayList<PlayerManagerListener>();

    public void switchTo(Player player) {
        if (this.player != null) {
            player.stop();
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
