package limma;

import limma.application.GeneralConfigImpl;
import limma.application.PlayerManager;
import limma.application.game.GameConfigImpl;
import limma.application.game.GamePlugin;
import limma.application.music.ExternalMusicPlayer;
import limma.application.music.MusicConfigImpl;
import limma.application.video.IMDBServiceImpl;
import limma.application.video.VideoConfigImpl;
import limma.application.video.VideoPlayer;
import limma.domain.music.XMLMusicRepositoryImpl;
import limma.domain.video.XMLVideoRepositoryImpl;
import limma.ui.*;
import limma.ui.browser.Navigation;
import limma.ui.browser.NavigationModel;
import limma.ui.browser.NavigationPopupMenu;
import limma.ui.dialogs.DialogFactory;
import limma.ui.dialogs.DialogManagerImpl;
import limma.ui.dialogs.LimmaDialog;
import limma.ui.music.MusicPlugin;
import limma.ui.video.EditMovieDialog;
import limma.ui.video.IMDBDialog;
import limma.ui.video.VideoPlugin;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        tweakSwing();

        final DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(KeyConfig.class);
        pico.registerComponentImplementation(CommandDispatcher.class);
        pico.registerComponentImplementation(GeneralConfigImpl.class);
        pico.registerComponentImplementation(ShuttingDownDialog.class);
        pico.registerComponentImplementation(UIPropertiesImpl.class);
        pico.registerComponentImplementation(PlayerManager.class);
        pico.registerComponentImplementation(NavigationModel.class);
        pico.registerComponentImplementation(Navigation.class);
        pico.registerComponentImplementation(NavigationPopupMenu.class);
        pico.registerComponentImplementation(DialogManagerImpl.class);
        pico.registerComponentImplementation(CursorHider.class);

        pico.registerComponentImplementation(XMLVideoRepositoryImpl.class);
        pico.registerComponentImplementation(VideoPlugin.class);
        pico.registerComponentImplementation(VideoConfigImpl.class);
        pico.registerComponentImplementation(VideoPlayer.class);
        pico.registerComponentImplementation(IMDBServiceImpl.class);
        pico.registerComponentImplementation(IMDBDialog.class);
        pico.registerComponentImplementation(EditMovieDialog.class);

        pico.registerComponentImplementation(XMLMusicRepositoryImpl.class);
        pico.registerComponentImplementation(MusicPlugin.class);
        pico.registerComponentImplementation(MusicConfigImpl.class);
//                pico.registerComponentImplementation(JavaSoundMusicPlayer.class);
        pico.registerComponentImplementation(ExternalMusicPlayer.class);

        pico.registerComponentImplementation(GamePlugin.class);
        pico.registerComponentImplementation(GameConfigImpl.class);

        pico.registerComponentImplementation(MainWindow.class);

        pico.registerComponentInstance(new DialogFactory() {
            public LimmaDialog createDialog(Class dialog) {
                return (LimmaDialog) pico.getComponentInstanceOfType(dialog);
            }
        });

        pico.start();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);

                if (useFullScreen()) {
                    mainWindow.setUndecorated(true);
                    mainWindow.setResizable(false);

                    GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                    device.setFullScreenWindow(mainWindow);
                    mainWindow.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());

                } else {
                    System.out.println("Starting in windowed mode");
                    mainWindow.setSize(800, 600);
                    mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    mainWindow.setVisible(true);
                }


                mainWindow.requestFocus();
            }
        });
    }

    private static boolean useFullScreen() {
        return !"true".equalsIgnoreCase(System.getProperty("limma.window"));
    }

    private static void tweakSwing() {
        System.setProperty("swing.aatext", "true");
        UIManager.put("TabbedPane.tabsOpaque", "false");
        UIManager.put("TabbedPane.contentOpaque", "false");
    }
}
