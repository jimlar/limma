package limma.plugins.game;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasLabel;
import limma.swing.AntialiasList;
import limma.swing.DialogManager;
import limma.swing.SimpleListModel;
import limma.swing.navigationlist.NavigationModel;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.PlayerManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class GamePlugin implements Plugin {
    private DialogManager dialogManager;
    private GameConfig gameConfig;
    private JTabbedPane tabbedPane;
    private boolean hasBeenEntered;

    private SimpleListModel c64GameListModel;
    private AntialiasList c64GameList;

    private SimpleListModel snesGameListModel;
    private AntialiasList snesGameList;

    public GamePlugin(DialogManager dialogManager, GameConfig gameConfig) {
        this.dialogManager = dialogManager;
        this.gameConfig = gameConfig;
    }

    public String getPluginName() {
        return "game";
    }

    public JComponent getPluginView() {
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
            dialogManager.executeInDialog(new LoadC64GamesTask(c64GameListModel, gameConfig));
            dialogManager.executeInDialog(new LoadSnesGamesTask(snesGameListModel, gameConfig));
        }
    }

    public boolean keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                return true;
            case KeyEvent.VK_ENTER:
                GameFile selectedGame = (GameFile) getVisibleGameList().getSelectedValue();
                execute(selectedGame);
                return true;

            case KeyEvent.VK_RIGHT:
                if (tabbedPane.getSelectedIndex() < tabbedPane.getTabCount() - 1) {
                    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() + 1);
                }
                return true;
            case KeyEvent.VK_LEFT:
                if (tabbedPane.getSelectedIndex() > 0) {
                    tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex() - 1);
                }
                return true;
        }
        getVisibleGameList().processKeyEvent(e);
        return true;
    }

    public void init(NavigationModel navigationModel, PlayerManager playerManager) {
        navigationModel.add(new DefaultNavigationNode("Games"));
    }

    private AntialiasList getVisibleGameList() {
        if (tabbedPane.getSelectedIndex() == 0) {
            return c64GameList;
        } else {
            return snesGameList;
        }
    }

    private void execute(GameFile game) {
        try {
            if (game.getType().equals(GameFile.C64)) {
                gameConfig.getC64Command().execute(game.getFile().getAbsolutePath());

            } else if (game.getType().equals(GameFile.SNES)) {
                gameConfig.getSnesCommand().execute(game.getFile().getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
