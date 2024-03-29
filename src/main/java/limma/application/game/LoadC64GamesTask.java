package limma.application.game;

import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;
import limma.utils.DirectoryScanner;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

class LoadC64GamesTask implements Task {
    private final File gamesDir;
    private SimpleBrowserNode c64Node;
    private GameConfig gameConfig;

    public LoadC64GamesTask(GameConfig gameConfig, SimpleBrowserNode c64Node) {
        this.c64Node = c64Node;
        this.gameConfig = gameConfig;
        this.gamesDir = gameConfig.getC64GamesDir();
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Loading C64 games from " + gamesDir.getAbsolutePath());
        final ArrayList<GameFile> files = new ArrayList<GameFile>();
        new DirectoryScanner(gamesDir).accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                String name = file.getName().toLowerCase();
                if (file.isFile() && (name.endsWith(".zip") || name.endsWith(".p00") || name.endsWith(".prg") || name.endsWith(".x64") || name.endsWith(".d64") || name.endsWith(".t64"))) {
                    files.add(new GameFile(getGameName(file), file, GameFile.C64));
                }
                return true;
            }
        });

        for (Iterator i = files.iterator(); i.hasNext();) {
            GameFile gameFile = (GameFile) i.next();
            c64Node.add(new GameBrowserNode(gameFile, gameConfig));
        }
        c64Node.sort();
    }

    private String getGameName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        return name.replace('_', ' ').replace('.', ' ');
    }
}
