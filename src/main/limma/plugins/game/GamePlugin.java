package limma.plugins.game;

import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.navigationlist.NavigationModel;
import limma.swing.navigationlist.NavigationNode;

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
        NavigationNode gamesNode = new NavigationNode("Games");
        navigationModel.add(gamesNode);

        NavigationNode c64Node = new NavigationNode("Commodore 64");
        gamesNode.add(c64Node);
        NavigationNode snesNode = new NavigationNode("Super Nintendo");
        gamesNode.add(snesNode);

        dialogManager.executeInDialog(new LoadC64GamesTask(gameConfig, c64Node));
        dialogManager.executeInDialog(new LoadSnesGamesTask(gameConfig, snesNode));
    }
}
