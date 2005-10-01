package limma.plugins.game;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.*;
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
    private DialogManager dialogManager;
    private JTabbedPane tabbedPane;
    private boolean hasBeenEntered;

    private SimpleListModel c64GameListModel;
    private AntialiasList c64GameList;

    private SimpleListModel snesGameListModel;
    private AntialiasList snesGameList;

    public GamePlugin(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    public String getPluginName() {
        return "game";
    }

    public JComponent getPluginComponent() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        tabbedPane = new JTabbedPane();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Games");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        tabbedPane.setBorder(titledBorder);
        tabbedPane.setOpaque(false);
        panel.add(tabbedPane, BorderLayout.CENTER);

        setupC64Tab();
        setupSnesTab();

        return panel;
    }

    private void setupSnesTab() {
        snesGameListModel = new SimpleListModel();
        snesGameList = new AntialiasList(snesGameListModel);
        JScrollPane scrollPane = new JScrollPane(snesGameList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        tabbedPane.addTab("SNES", scrollPane);
    }

    private void setupC64Tab() {
        c64GameListModel = new SimpleListModel();
        c64GameList = new AntialiasList(c64GameListModel);
        JScrollPane scrollPane = new JScrollPane(c64GameList);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        tabbedPane.addTab("C64", scrollPane);
    }

    public void pluginEntered() {
        if (!hasBeenEntered) {
            hasBeenEntered = true;
            dialogManager.executeInDialog(new LoadC64GamesTask(c64GameListModel));
            dialogManager.executeInDialog(new LoadSnesGamesTask(snesGameListModel));
        }
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
            case KeyEvent.VK_ENTER:
                GameFile selectedGame = (GameFile) c64GameList.getSelectedValue();
                execute(selectedGame);
                break;

            case KeyEvent.VK_RIGHT:
                if (tabbedPane.getSelectedIndex() < tabbedPane.getTabCount() - 1) {
                    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
                }
                break;
            case KeyEvent.VK_LEFT:
                if (tabbedPane.getSelectedIndex() > 0) {
                    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
                }
                break;

        }
        c64GameList.processKeyEvent(e);
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

    private static class LoadC64GamesTask implements Task {
        private final File gamesDir;
        private SimpleListModel listModel;

        public LoadC64GamesTask(SimpleListModel listModel) {
            this.listModel = listModel;
            this.gamesDir = new File("/media/games/c64");
        }

        public JComponent createComponent() {
            return new AntialiasLabel("Loading C64 games from " + gamesDir.getAbsolutePath());
        }

        public void run() {
            final ArrayList files = new ArrayList();
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

    private static class LoadSnesGamesTask implements Task {
        private final File gamesDir;
        private SimpleListModel listModel;

        public LoadSnesGamesTask(SimpleListModel listModel) {
            this.listModel = listModel;
            this.gamesDir = new File("/media/games/snes");
        }

        public JComponent createComponent() {
            return new AntialiasLabel("Loading SNES games from " + gamesDir.getAbsolutePath());
        }

        public void run() {
            final ArrayList files = new ArrayList();
            new DirectoryScanner(gamesDir).accept(new DirectoryScanner.Visitor() {
                public void visit(File file) {
                    String name = file.getName().toLowerCase();
                    if (name.endsWith(".zip") || name.endsWith(".smc") || name.endsWith(".fig")) {
                        files.add(new GameFile(getGameName(file), file));
                    }
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
}
