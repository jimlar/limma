package limma.plugins.game;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasLabel;
import limma.swing.AntialiasList;
import limma.swing.SimpleListModel;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class GamePlugin implements Plugin {
    private SimpleListModel gameListModel;
    private AntialiasList gameList;
    private boolean hasBeenEntered;

    public String getPluginName() {
        return "game";
    }

    public JComponent getPluginComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        gameListModel = new SimpleListModel();
        gameList = new AntialiasList(gameListModel);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Games");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        JScrollPane scrollPane = new JScrollPane(gameList);
        scrollPane.setBorder(titledBorder);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    public void pluginEntered() {
        if (!hasBeenEntered) {
            hasBeenEntered = true;
            final ArrayList files = new ArrayList();
            File gamesDir = new File("/media/games/c64");
            new DirectoryScanner(gamesDir).accept(new DirectoryScanner.Visitor() {
                public void visit(File file) {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".zip") || name.endsWith(".p00") || name.endsWith(".prg") || name.endsWith(".x64") || name.endsWith(".d64") || name.endsWith(".t64")) {
                        files.add(new GameFile(getGameName(file), file));
                    }
                }
            });
            Collections.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((GameFile) o1).getName().compareToIgnoreCase(((GameFile) o2).getName());
                }
            });
            gameListModel.setObjects(files);
        }
    }

    private String getGameName(File file) {
        String name = file.getName();
        name = name.substring(0, name.lastIndexOf('.'));
        return name.replace('_', ' ').replace('.', ' ');
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
            case KeyEvent.VK_ENTER:
                GameFile selectedGame = (GameFile) gameList.getSelectedValue();
                execute(selectedGame);
                break;

        }
        gameList.processKeyEvent(e);
    }

    private void execute(GameFile game) {
        try {
            Runtime.getRuntime().exec(new String[]{
                "/usr/games/bin/x64",
                game.getFile().getAbsolutePath()
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class GameFile {
        private String name;
        private File file;

        public GameFile(String name, File file) {
            this.name = name;
            this.file = file;
        }

        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public File getFile() {
            return file;
        }
    }
}
