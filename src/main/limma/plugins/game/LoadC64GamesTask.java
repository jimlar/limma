package limma.plugins.game;

import limma.swing.AntialiasLabel;
import limma.swing.SimpleListModel;
import limma.swing.Task;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class LoadC64GamesTask implements Task {
    private final File gamesDir;
    private SimpleListModel listModel;

    public LoadC64GamesTask(SimpleListModel listModel, GameConfig gameConfig) {
        this.listModel = listModel;
        this.gamesDir = gameConfig.getC64GamesDir();
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading C64 games from " + gamesDir.getAbsolutePath());
    }

    public void run() {
        final ArrayList files = new ArrayList();
        new DirectoryScanner(gamesDir).accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                String name = file.getName().toLowerCase();
                if (file.isFile() && (name.endsWith(".zip") || name.endsWith(".p00") || name.endsWith(".prg") || name.endsWith(".x64") || name.endsWith(".d64") || name.endsWith(".t64"))) {
                    files.add(new GameFile(getGameName(file), file, GameFile.C64));
                }
                return true;
            }
        });
        Collections.sort(files, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((GameFile) o1).getName().compareToIgnoreCase(((GameFile) o2).getName());
            }
        });
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                listModel.setObjects(files);
            }
        });
    }

    private String getGameName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        return name.replace('_', ' ').replace('.', ' ');
    }
}
