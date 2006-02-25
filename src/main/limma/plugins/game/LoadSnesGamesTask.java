package limma.plugins.game;

import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.swing.TaskInfo;
import limma.swing.menu.SimpleMenuNode;
import limma.utils.DirectoryScanner;
import limma.UIProperties;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

class LoadSnesGamesTask implements Task {
    private final File gamesDir;
    private SimpleMenuNode snesNode;
    private GameConfig gameConfig;
    private UIProperties uiProperties;

    public LoadSnesGamesTask(GameConfig gameConfig, SimpleMenuNode snesNode, UIProperties uiProperties) {
        this.snesNode = snesNode;
        this.gameConfig = gameConfig;
        this.uiProperties = uiProperties;
        this.gamesDir = gameConfig.getSnesGamesDir();
    }

    public JComponent prepareToRun(TaskInfo taskInfo) {
        return new AntialiasLabel("Loading SNES games from " + gamesDir.getAbsolutePath(), uiProperties);
    }

    public void run() {
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
            snesNode.add(new GameMenuNode(gameFile, gameConfig));
        }
        snesNode.sortByTitle();
    }

    private String getGameName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        return name.replace('_', ' ').replace('.', ' ');
    }
}
