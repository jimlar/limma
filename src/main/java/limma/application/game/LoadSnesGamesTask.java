package limma.application.game;

import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;
import limma.utils.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

class LoadSnesGamesTask implements Task {
    private final File gamesDir;
    private SimpleBrowserNode snesNode;
    private GameConfig gameConfig;

    public LoadSnesGamesTask(GameConfig gameConfig, SimpleBrowserNode snesNode) {
        this.snesNode = snesNode;
        this.gameConfig = gameConfig;
        this.gamesDir = gameConfig.getSnesGamesDir();
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Loading SNES games from " + gamesDir.getAbsolutePath());
        final ArrayList<GameFile> files = new ArrayList<GameFile>();
        new DirectoryScanner(gamesDir).accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                String name = file.getName().toLowerCase();
                if (file.isFile() && (name.endsWith(".zip") || name.endsWith(".smc") || name.endsWith(".fig"))) {
                    files.add(new GameFile(getGameName(file), file, GameFile.SNES));
                }
                return true;
            }
        });

        snesNode.removeAllChildren();
        for (Iterator i = files.iterator(); i.hasNext();) {
            GameFile gameFile = (GameFile) i.next();
            snesNode.add(new GameBrowserNode(gameFile, gameConfig));
        }
        snesNode.sort();
    }

    private String getGameName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        return name.replace('_', ' ').replace('.', ' ');
    }
}
