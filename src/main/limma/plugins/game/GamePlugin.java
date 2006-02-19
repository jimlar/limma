package limma.plugins.game;

import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.menu.MenuModel;
import limma.swing.menu.SimpleMenuNode;
import limma.UIProperties;

public class GamePlugin implements Plugin {
    private DialogManager dialogManager;
    private GameConfig gameConfig;
    private MenuModel menuModel;
    private UIProperties uiProperties;

    public GamePlugin(DialogManager dialogManager, GameConfig gameConfig, MenuModel menuModel, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.gameConfig = gameConfig;
        this.menuModel = menuModel;
        this.uiProperties = uiProperties;
    }

    public void init() {
        SimpleMenuNode gamesNode = new SimpleMenuNode("Games");
        menuModel.add(gamesNode);

        SimpleMenuNode c64Node = new SimpleMenuNode("Commodore 64");
        gamesNode.add(c64Node);
        SimpleMenuNode snesNode = new SimpleMenuNode("Super Nintendo");
        gamesNode.add(snesNode);

        dialogManager.executeInDialog(new LoadC64GamesTask(gameConfig, c64Node, uiProperties));
        dialogManager.executeInDialog(new LoadSnesGamesTask(gameConfig, snesNode, uiProperties));
    }
}
