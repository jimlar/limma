package limma;

import limma.plugins.Plugin;
import limma.swing.DialogManager;
import limma.swing.ImagePanel;
import limma.swing.LimmaDialog;
import limma.swing.menu.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainWindow extends JFrame {
    private JPanel mainPanel;
    private DialogManager dialogManager;

    private PlayerManager playerManager;

    private Navigation navigation;

    public MainWindow(DialogManager dialogManager, Plugin[] plugins, PlayerManager playerManager, NavigationModel navigationModel, Navigation navigation, UIProperties uiProperties) {
        this.dialogManager = dialogManager;
        this.playerManager = playerManager;
        this.navigation = navigation;

        final SlidePanel slidePanel = addSliderPanel();
        final Timer slideInPlayerTimer = new Timer(2 * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                slidePanel.slideIn();
            }
        });
        slideInPlayerTimer.setCoalesce(true);
        slideInPlayerTimer.setRepeats(false);

        playerManager.addListener(new PlayerManagerListener() {
            public void playerSwitched(Player player) {
                slidePanel.slideIn(player.getPlayerPane());
            }
        });

        navigation.addMenuListener(new NavigationListener() {
            public void navigationNodeFocusChanged(Navigation menu, NavigationNode newFocusedItem) {
                slidePanel.slideOut();
                slideInPlayerTimer.restart();
            }
        });

        setLayout(new BorderLayout());
        this.add(dialogManager.getDialogManagerComponent(), BorderLayout.CENTER);

        ImageIcon background = new ImageIcon(uiProperties.getBackgroundImage());
        mainPanel = new ImagePanel(background);
        mainPanel.setLayout(new BorderLayout());

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

        JScrollPane scrollPane = new JScrollPane(this.navigation);
        scrollPane.setOpaque(false);
        scrollPane.setAutoscrolls(true);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

        mainPanel.add(new HeaderPanel(uiProperties, navigation), BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        dialogManager.setRoot(mainPanel);

        validate();

        for (int i = 0; i < plugins.length; i++) {
            Plugin plugin = plugins[i];
            plugin.init();
        }

        navigationModel.add(new SimpleNavigationNode("Exit") {
            public void performAction() {
                System.exit(0);
            }
        });
    }

    private SlidePanel addSliderPanel() {
        final SlidePanel slidePanel = new SlidePanel();
        JPanel glassPane = (JPanel) getGlassPane();
        glassPane.setLayout(new BorderLayout());
        glassPane.setVisible(true);

        glassPane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.gridx = 1;
        glassPane.add(slidePanel, gbc);
        gbc.gridx = 0;
        gbc.weighty = Integer.MAX_VALUE;
        gbc.weightx = Integer.MAX_VALUE;
        glassPane.add(Box.createGlue(), gbc);
        return slidePanel;
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
//                    if (limmaMenu.isOpen()) {
//                        limmaMenu.close();
//                    } else {
//                        limmaMenu.open();
//                    }
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
                default:
                    navigation.processKeyEvent(e);
                    return true;
            }
        }
    }

    private Player getCurrentPlayer() {
        return playerManager.getPlayer();
    }
}
