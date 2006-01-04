package limma;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;
import limma.swing.navigationlist.DefaultNavigationNode;
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
    private JPanel cardPanel;

    public MainWindow2(DialogManager dialogManager, Plugin[] plugins) {
        this.dialogManager = dialogManager;

        this.setContentPane(dialogManager.getDialogManagerComponent());

//        ImageIcon background = new ImageIcon("background.jpg");
//        mainPanel = new ImagePanel(background);

        mainPanel = new JPanel();
        mainPanel.setBackground(Color.white);
        mainPanel.setOpaque(false);
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

        NavigationModel navigationModel = new NavigationModel();
        navigationList = new NavigationList(navigationModel);
        JScrollPane scrollPane = new JScrollPane(navigationList);
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        cardPanel.add(scrollPane, "menu");
        cardPanel.add(new JLabel("Limma"), "player");

        mainPanel.add(new HeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        dialogManager.setRoot(mainPanel);

        validate();
        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            plugin.init(navigationModel, this);
        }

        navigationModel.add(new DefaultNavigationNode("Exit") {
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

    public void switchTo(Player player) {
        if (currentPlayerPane != null) {
            cardPanel.remove(currentPlayerPane);
        }
        this.currentPlayer = player;
        currentPlayerPane = currentPlayer.getPlayerPane();
        if (currentPlayerPane != null) {
            cardPanel.add(currentPlayerPane, "player");
        }
    }

    private boolean dispatchKey(KeyEvent e) {
        LimmaDialog topDialog = dialogManager.getTopDialog();
        if (topDialog != null) {
            return topDialog.keyPressed(e);

        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_S:
                    currentPlayer.stop();
                    return true;
                case KeyEvent.VK_M:
                    if (isMenuOpen()) {
                        closeMenu();
                    } else {
                        openMenu();
                    }
                    return true;
                case KeyEvent.VK_N:
                    currentPlayer.next();
                    return true;
                case KeyEvent.VK_P:
                    currentPlayer.previous();
                    return true;
                case KeyEvent.VK_F:
                    currentPlayer.ff();
                    return true;
                case KeyEvent.VK_R:
                    currentPlayer.rew();
                    return true;
                case KeyEvent.VK_SPACE:
                    currentPlayer.pause();
                    return true;
            }
        }
        if (isMenuOpen()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE:
                    closeMenu();
                    return true;
                default:
                    navigationList.processKeyEvent(e);
                    return true;
            }
        } else {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DOWN:
                    currentPlayer.next();
                    return true;
                case KeyEvent.VK_UP:
                    currentPlayer.previous();
                    return true;
                case KeyEvent.VK_RIGHT:
                    currentPlayer.ff();
                    return true;
                case KeyEvent.VK_LEFT:
                    currentPlayer.rew();
                    return true;
            }
        }
        return false;
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
