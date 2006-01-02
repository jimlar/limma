package limma;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.DialogManager;
import limma.swing.ImagePanel;
import limma.swing.LimmaDialog;
import limma.swing.navigationlist.NavigationList;
import limma.swing.navigationlist.NavigationModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow2 extends JFrame implements PluginManager, PlayerManager {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;
    private DialogManager dialogManager;

    private Player currentPlayer;
    private JComponent currentPlayerPane;
    private NavigationList navigationList;
    private boolean menuOpen = false;

    public MainWindow2(GraphicsDevice graphicsDevice, DialogManager dialogManager, Plugin[] plugins) {
        this.dialogManager = dialogManager;
        ImageIcon background = new ImageIcon("background.jpg");

        this.setContentPane(dialogManager.getDialogManagerComponent());

        mainPanel = new ImagePanel(background);
        mainPanel.setOpaque(false);
        pluginCardsManager = new CardLayout(10, 10);
        mainPanel.setLayout(pluginCardsManager);
        dialogManager.setRoot(mainPanel);
        mainPanel.setSize(graphicsDevice.getDisplayMode().getWidth(), graphicsDevice.getDisplayMode().getHeight());

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    boolean b = dispatchKey(e);
                    if (b) {
                        e.consume();
                    }
                    return b;
                }
                return false;
            }
        });

        NavigationModel navigationModel = new NavigationModel();
        navigationList = new NavigationList(navigationModel);
        JScrollPane scrollPane = new JScrollPane(navigationList);
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainPanel.add(scrollPane, "menu");
        mainPanel.add(new JLabel("Limma"), "player");

        validate();
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            plugin.init(navigationModel, this);
        }

        openMenu();
    }

    public void switchTo(Player player) {
        if (currentPlayerPane != null) {
            mainPanel.remove(currentPlayerPane);
        }
        this.currentPlayer = player;
        currentPlayerPane = currentPlayer.getPlayerPane();
        if (currentPlayerPane != null) {
            mainPanel.add(currentPlayerPane, "player");
        }
    }

    private boolean dispatchKey(KeyEvent e) {
        LimmaDialog topDialog = dialogManager.getTopDialog();
        if (topDialog != null) {
            return topDialog.keyPressed(e);
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    closeMenu();
                    return true;
                case KeyEvent.VK_S:
                    currentPlayer.stop();
                    return true;
                case KeyEvent.VK_M:
                    openMenu();
                    return true;
                case KeyEvent.VK_N:
                    currentPlayer.next();
                    return true;
            }
        }
        navigationList.processKeyEvent(e);
        return true;
    }

    private boolean isMenuOpen() {
        return menuOpen;
    }

    private void openMenu() {
        menuOpen = true;
        pluginCardsManager.show(mainPanel, "menu");
    }

    private void closeMenu() {
        menuOpen = false;
        pluginCardsManager.show(mainPanel, "player");
    }

    /**
     * Will be removed
     */
    public void enterPlugin(String name) {
    }

    /**
     * Will be removed
     */
    public void exitPlugin() {
    }
}
