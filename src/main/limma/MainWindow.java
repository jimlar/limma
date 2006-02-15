package limma;

import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;
import limma.swing.ImagePanel;
import limma.swing.navigationlist.MenuModel;
import limma.swing.navigationlist.MenuNode;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
    private CardLayout pluginCardsManager;
    private JPanel mainPanel;
    private DialogManager dialogManager;

    private PlayerManager playerManager;
    private JComponent currentPlayerPane;

    private limma.swing.navigationlist.LimmaMenu limmaMenu;
    private boolean menuOpen = false;
    private JPanel cardPanel;

    public MainWindow(DialogManager dialogManager, Plugin[] plugins, PlayerManager playerManager, MenuModel menuModel, limma.swing.navigationlist.LimmaMenu limmaMenu, UIProperties uiProperties) {
        this.dialogManager = dialogManager;

        this.playerManager = playerManager;
        playerManager.addListener(new PlayerManagerListener() {
            public void playerSwitched(Player player) {
                if (currentPlayerPane != null) {
                    cardPanel.remove(currentPlayerPane);
                }
                currentPlayerPane = player.getPlayerPane();
                if (currentPlayerPane != null) {
                    cardPanel.add(currentPlayerPane, "player");
                }
            }
        });

        setLayout(new BorderLayout());
        this.add(dialogManager.getDialogManagerComponent(), BorderLayout.CENTER);

        ImageIcon background = new ImageIcon(uiProperties.getBackDropImage());
        mainPanel = new ImagePanel(background);

//        mainPanel = new JPanel();
//        mainPanel.setBackground(Color.white);
//        mainPanel.setOpaque(true);
        
        mainPanel.setLayout(new BorderLayout());

        pluginCardsManager = new CardLayout(0, 0);
        cardPanel = new JPanel(pluginCardsManager);
        cardPanel.setOpaque(false);

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

        this.limmaMenu = limmaMenu;
        JScrollPane scrollPane = new JScrollPane(this.limmaMenu);
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cardPanel.add(scrollPane, "menu");

        JLabel defaultPlayer = new JLabel();
        defaultPlayer.setOpaque(false);
        cardPanel.add(defaultPlayer, "player");

        mainPanel.add(new HeaderPanel(uiProperties), BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);

        dialogManager.setRoot(mainPanel);

        validate();

        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            plugin.init();
        }

        menuModel.add(new MenuNode("Exit") {
            public void performAction() {
                System.exit(0);
            }
        });
        openMenu();
    }

    public void setSize(int width, int height) {
        super.setSize(width, height);
        mainPanel.setSize(width, height);
    }

    private boolean dispatchKey(KeyEvent e) {
        LimmaDialog topDialog = dialogManager.getTopDialog();
        if (topDialog != null) {
            return topDialog.keyPressed(e);

        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_S:
                    getCurrentPlayer().stop();
                    return true;
                case KeyEvent.VK_M:
                    if (isMenuOpen()) {
                        closeMenu();
                    } else {
                        openMenu();
                    }
                    return true;
                case KeyEvent.VK_N:
                    getCurrentPlayer().next();
                    return true;
                case KeyEvent.VK_P:
                    getCurrentPlayer().previous();
                    return true;
                case KeyEvent.VK_F:
                    getCurrentPlayer().ff();
                    return true;
                case KeyEvent.VK_R:
                    getCurrentPlayer().rew();
                    return true;
                case KeyEvent.VK_SPACE:
                    getCurrentPlayer().pause();
                    return true;
            }
        }
        if (isMenuOpen()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    closeMenu();
                    return true;
                default:
                    limmaMenu.processKeyEvent(e);
                    return true;
            }
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    getCurrentPlayer().next();
                    return true;
                case KeyEvent.VK_UP:
                    getCurrentPlayer().previous();
                    return true;
                case KeyEvent.VK_RIGHT:
                    getCurrentPlayer().ff();
                    return true;
                case KeyEvent.VK_LEFT:
                    getCurrentPlayer().rew();
                    return true;
            }
        }
        return false;
    }

    private Player getCurrentPlayer() {
        return playerManager.getPlayer();
    }

    private boolean isMenuOpen() {
        return menuOpen;
    }

    private void openMenu() {
        menuOpen = true;
        pluginCardsManager.show(cardPanel, "menu");
    }

    private void closeMenu() {
        menuOpen = false;
        pluginCardsManager.show(cardPanel, "player");
    }
}
