package limma.application.game;

import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;

import java.io.IOException;

public class GameBrowserNode extends SimpleBrowserNode {
    private GameFile game;
    private GameConfig gameConfig;

    public GameBrowserNode(GameFile game, GameConfig gameConfig) {
        super(game.getName());
        this.game = game;
        this.gameConfig = gameConfig;
    }

    public void performAction(DialogManager dialogManager) {
        try {
            if (game.getType().equals(GameFile.C64)) {
                gameConfig.getC64Command().execute(game.getFile().getAbsolutePath());

            } else if (game.getType().equals(GameFile.SNES)) {
                gameConfig.getSnesCommand().execute(game.getFile().getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
