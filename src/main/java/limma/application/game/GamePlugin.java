package limma.application.game;

import limma.application.Plugin;
import limma.ui.browser.model.BrowserModel;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;

public class GamePlugin implements Plugin {
    private DialogManager dialogManager;
    private GameConfig gameConfig;
    private BrowserModel browserModel;

    public GamePlugin(DialogManager dialogManager, GameConfig gameConfig, BrowserModel browserModel) {
        this.dialogManager = dialogManager;
        this.gameConfig = gameConfig;
        this.browserModel = browserModel;
    }

    public void init() {
        SimpleBrowserNode gamesNode = new SimpleBrowserNode("Games");
        browserModel.add(gamesNode);

        SimpleBrowserNode c64Node = new SimpleBrowserNode("Commodore 64");
        gamesNode.add(c64Node);
        SimpleBrowserNode snesNode = new SimpleBrowserNode("Super Nintendo");
        gamesNode.add(snesNode);

        dialogManager.executeInDialog(new LoadC64GamesTask(gameConfig, c64Node));
        dialogManager.executeInDialog(new LoadSnesGamesTask(gameConfig, snesNode));
    }
}
