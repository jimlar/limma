package limma.application.game;

import limma.utils.ExternalCommand;

import java.io.File;

public interface GameConfig {
    File getC64GamesDir();

    ExternalCommand getC64Command();

    File getSnesGamesDir();

    ExternalCommand getSnesCommand();
}
