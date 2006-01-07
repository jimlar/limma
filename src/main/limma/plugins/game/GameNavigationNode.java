package limma.plugins.game;

import limma.swing.navigationlist.DefaultNavigationNode;

import java.io.IOException;

public class GameNavigationNode extends DefaultNavigationNode {
    private GameFile game;
    private GameConfig gameConfig;

    public GameNavigationNode(GameFile game, GameConfig gameConfig) {
        super(game.getName());
        this.game = game;
        this.gameConfig = gameConfig;
    }

    public void performAction() {
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