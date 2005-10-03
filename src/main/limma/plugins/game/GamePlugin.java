package limma.plugins.game;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasLabel;
import limma.swing.AntialiasList;
import limma.swing.DialogManager;
import limma.swing.SimpleListModel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

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
        tabbedPane.setFont(AntialiasLabel.DEFAULT_FONT);
        tabbedPane.setForeground(Color.white);
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
                GameFile selectedGame = (GameFile) getVisibleGameList().getSelectedValue();
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
        getVisibleGameList().processKeyEvent(e);
    }

    private AntialiasList getVisibleGameList() {
        if (tabbedPane.getSelectedIndex() == 0) {
            return c64GameList;
        } else {
            return snesGameList;
        }
    }

    private void execute(GameFile game) {
        Runtime runtime = Runtime.getRuntime();
        try {
            if (game.getType().equals(GameFile.C64)) {
                runtime.exec(new String[]{"/usr/games/bin/x64", game.getFile().getAbsolutePath()});

            } else if (game.getType().equals(GameFile.SNES)) {
                runtime.exec(new String[]{"/usr/games/bin/zsnes", "-z", "-r", "4", "-m", "-j", game.getFile().getAbsolutePath()});

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
