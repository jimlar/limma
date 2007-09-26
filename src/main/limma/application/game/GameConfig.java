package limma.application.game;

import java.io.File;

import limma.utils.ExternalCommand;

public interface GameConfig {
    File getC64GamesDir();

    ExternalCommand getC64Command();

    File getSnesGamesDir();

    ExternalCommand getSnesCommand();
}
