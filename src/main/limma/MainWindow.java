package limma;

import limma.application.Plugin;
import limma.ui.ImagePanel;
import limma.ui.Player;
import limma.ui.UIProperties;
import limma.ui.browser.Navigation;
import limma.ui.browser.NavigationListener;
import limma.ui.browser.NavigationModel;
import limma.ui.browser.NavigationNode;
import limma.ui.dialogs.DialogManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MainWindow extends JFrame {
    private JPanel mainPanel;

    public MainWindow(DialogManager dialogManager, Plugin[] plugins, PlayerManager playerManager, NavigationModel navigationModel, Navigation navigation, UIProperties uiProperties, final GeneralConfig generalConfig) {
        final SlidePanel playerSlidePanel = addPlayerSlidePanel();
        final Timer slideInPlayerTimer = new Timer(2 * 1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                playerSlidePanel.slideIn();
            }
        });
        slideInPlayerTimer.setCoalesce(true);
        slideInPlayerTimer.setRepeats(false);

        playerManager.addListener(new PlayerManagerListener() {
            public void playerSwitched(Player player) {
                playerSlidePanel.slideIn(player.getPlayerPane());
            }
        });

        navigation.addNavigationListener(new NavigationListener() {
            public void navigationNodeFocusChanged(Navigation menu, NavigationNode newFocusedItem) {
                playerSlidePanel.slideOut();
                slideInPlayerTimer.restart();
            }
        });

        setLayout(new BorderLayout());
        this.add(dialogManager.getDialogManagerComponent(), BorderLayout.CENTER);

        ImageIcon background = new ImageIcon(uiProperties.getBackgroundImage());
        mainPanel = new ImagePanel(background);
        mainPanel.setLayout(new BorderLayout());

        JScrollPane scrollPane = new JScrollPane(navigation);
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

        navigationModel.add(new limma.ui.browser.MenuItem("Shutdown") {
            public void performAction(DialogManager dialogManager) {
                try {
                    generalConfig.getShutdownCommand().execute();
                    dialogManager.createAndOpen(ShuttingDownDialog.class);
                } catch (IOException e) {
                    System.out.println("Could not shutdown");
                    e.printStackTrace();
                }
            }
        });
    }

    private SlidePanel addPlayerSlidePanel() {
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
}
