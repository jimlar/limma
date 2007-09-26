package limma;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import limma.application.game.GameConfigImpl;
import limma.application.game.GamePlugin;
import limma.application.music.ExternalMusicPlayer;
import limma.application.music.MusicConfigImpl;
import limma.application.music.MusicPlugin;
import limma.application.video.*;
import limma.domain.music.XMLMusicRepositoryImpl;
import limma.domain.video.XMLVideoRepositoryImpl;
import limma.ui.CursorHider;
import limma.ui.UIPropertiesImpl;
import limma.ui.browser.Navigation;
import limma.ui.browser.NavigationModel;
import limma.ui.browser.NavigationPopupMenu;
import limma.ui.dialogs.DialogFactory;
import limma.ui.dialogs.DialogManagerImpl;
import limma.ui.dialogs.LimmaDialog;
import org.picocontainer.defaults.DefaultPicoContainer;

public class Main {

    public static void main(String[] args) {
        tweakSwing();

        final DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(KeyConfig.class);
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
