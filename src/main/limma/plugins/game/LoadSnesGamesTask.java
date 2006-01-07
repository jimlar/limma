package limma.plugins.game;

import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.swing.navigationlist.NavigationNode;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

class LoadSnesGamesTask implements Task {
    private final File gamesDir;
    private NavigationNode snesNode;
    private GameConfig gameConfig;

    public LoadSnesGamesTask(GameConfig gameConfig, NavigationNode snesNode) {
        this.snesNode = snesNode;
        this.gameConfig = gameConfig;
        this.gamesDir = gameConfig.getSnesGamesDir();
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading SNES games from " + gamesDir.getAbsolutePath());
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
            snesNode.add(new GameNavigationNode(gameFile, gameConfig));
        }
        snesNode.sortByTitle();
    }

    private String getGameName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        return name.replace('_', ' ').replace('.', ' ');
    }
}
