package limma.plugins.game;

import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.swing.navigationlist.NavigationModel;

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
        DefaultNavigationNode gamesNode = new DefaultNavigationNode("Games");
        navigationModel.add(gamesNode);

        DefaultNavigationNode c64Node = new DefaultNavigationNode("Commodore 64");
        gamesNode.add(c64Node);
        DefaultNavigationNode snesNode = new DefaultNavigationNode("Super Nintendo");
        gamesNode.add(snesNode);

        dialogManager.executeInDialog(new LoadC64GamesTask(gameConfig, c64Node));
        dialogManager.executeInDialog(new LoadSnesGamesTask(gameConfig, snesNode));
    }
}
