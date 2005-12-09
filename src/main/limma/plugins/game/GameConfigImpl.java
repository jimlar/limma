package limma.plugins.game;

import limma.AbstractConfiguration;
import limma.utils.ExternalCommand;

import java.io.File;

public class GameConfigImpl extends AbstractConfiguration implements GameConfig {

    public GameConfigImpl() {
        super("game");
    }

    public File getC64GamesDir() {
        return getFile("c64.gamesdir");
    }

    public ExternalCommand getC64Command() {
        return new ExternalCommand(getString("c64.command"));
    }

    public File getSnesGamesDir() {
        return getFile("snes.gamesdir");
    }

    public ExternalCommand getSnesCommand() {
        return new ExternalCommand(getString("snes.command"));
    }
}
