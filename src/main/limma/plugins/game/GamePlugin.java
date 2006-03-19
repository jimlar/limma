package limma.plugins.game;

import limma.UIProperties;
import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.NavigationModel;
import limma.swing.menu.SimpleNavigationNode;

public class GamePlugin implements Plugin {
    private DialogManager dialogManager;
    private GameConfig gameConfig;
    private NavigationModel navigationModel;
    private UIProperties uiProperties;

    public GamePlugin(DialogManager dialogManager, GameConfig gameConfig, NavigationModel navigationModel, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.gameConfig = gameConfig;
        this.navigationModel = navigationModel;
        this.uiProperties = uiProperties;
    }

    public void init() {
        SimpleNavigationNode gamesNode = new SimpleNavigationNode("Games");
        navigationModel.add(gamesNode);

        SimpleNavigationNode c64Node = new SimpleNavigationNode("Commodore 64");
        gamesNode.add(c64Node);
        SimpleNavigationNode snesNode = new SimpleNavigationNode("Super Nintendo");
        gamesNode.add(snesNode);

        dialogManager.executeInDialog(new LoadC64GamesTask(gameConfig, c64Node, uiProperties));
        dialogManager.executeInDialog(new LoadSnesGamesTask(gameConfig, snesNode, uiProperties));
    }
}
