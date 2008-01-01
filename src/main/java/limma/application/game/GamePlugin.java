package limma.application.game;

import limma.application.Plugin;
import limma.ui.browser.NavigationModel;
import limma.ui.browser.SimpleNavigationNode;
import limma.ui.dialogs.DialogManager;

public class GamePlugin implements Plugin {
    private DialogManager dialogManager;
    private GameConfig gameConfig;
    private NavigationModel navigationModel;

    public GamePlugin(DialogManager dialogManager, GameConfig gameConfig, NavigationModel navigationModel) {
        this.dialogManager = dialogManager;
        this.gameConfig = gameConfig;
        this.navigationModel = navigationModel;
    }

    public void init() {
        SimpleNavigationNode gamesNode = new SimpleNavigationNode("Games");
        navigationModel.add(gamesNode);

        SimpleNavigationNode c64Node = new SimpleNavigationNode("Commodore 64");
        gamesNode.add(c64Node);
        SimpleNavigationNode snesNode = new SimpleNavigationNode("Super Nintendo");
        gamesNode.add(snesNode);

        dialogManager.executeInDialog(new LoadC64GamesTask(gameConfig, c64Node));
        dialogManager.executeInDialog(new LoadSnesGamesTask(gameConfig, snesNode));
    }
}
