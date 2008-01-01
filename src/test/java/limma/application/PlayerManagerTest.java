package limma.application;

import com.agical.rmock.extension.junit.RMockTestCase;
import limma.ui.Player;

public class PlayerManagerTest extends RMockTestCase {

    public void testPlayerManagerStopsCurrentPlayerWhenSwitchingToNew() throws Exception {
        PlayerManager playerManager = new PlayerManager();
        Player player = (Player) mock(Player.class);

        player.consume(Command.STOP);

        startVerification();
        playerManager.switchTo(player);
        playerManager.switchTo(player);
    }

    public void testPlayerManagerStopsCurrentPlayerButNotTheNewWhenSwitchingToNew() throws Exception {
        PlayerManager playerManager = new PlayerManager();
        Player player = (Player) mock(Player.class);

        startVerification();
        playerManager.switchTo(player);
    }
}
