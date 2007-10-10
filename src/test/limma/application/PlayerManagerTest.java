package limma.application;

import limma.ui.Player;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;

public class PlayerManagerTest extends MockObjectTestCase {

    public void testPlayerManagerStopsCurrentPlayerWhenSwitchingToNew() throws Exception {
        PlayerManager playerManager = new PlayerManager();
        Mock playerMock = mock(Player.class);

        playerMock.expects(never()).method("stop");
        playerManager.switchTo((Player) playerMock.proxy());
        playerMock.verify();
        playerMock.expects(once()).method("stop");
        playerManager.switchTo((Player) playerMock.proxy());
        playerMock.verify();
    }
}
