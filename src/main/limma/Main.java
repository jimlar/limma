package limma;

import limma.persistence.PersistenceConfigImpl;
import limma.persistence.PersistenceManagerImpl;
import limma.plugins.game.GameConfigImpl;
import limma.plugins.game.GamePlugin;
import limma.plugins.music.ExternalMusicPlayer;
import limma.plugins.music.MusicConfigImpl;
import limma.plugins.music.MusicPlugin;
import limma.plugins.video.*;
import limma.swing.CursorHider;
import limma.swing.DialogFactory;
import limma.swing.DialogManagerImpl;
import limma.swing.LimmaDialog;
import limma.swing.menu.NavigationModel;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        tweakSwing();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                final DefaultPicoContainer pico = new DefaultPicoContainer();
                pico.registerComponentImplementation(UIPropertiesImpl.class);
                pico.registerComponentImplementation(PlayerManager.class);
                pico.registerComponentImplementation(NavigationModel.class);
                pico.registerComponentImplementation(limma.swing.menu.Navigation.class);
                pico.registerComponentImplementation(PersistenceManagerImpl.class);
                pico.registerComponentImplementation(PersistenceConfigImpl.class);
                pico.registerComponentImplementation(DialogManagerImpl.class);
                pico.registerComponentImplementation(CursorHider.class);

                pico.registerComponentImplementation(VideoPlugin.class);
                pico.registerComponentImplementation(VideoConfigImpl.class);
                pico.registerComponentImplementation(VideoPlayer.class);
                pico.registerComponentImplementation(IMDBServiceImpl.class);
                pico.registerComponentImplementation(IMDBDialog.class);
                pico.registerComponentImplementation(EditMovieDialog.class);

                pico.registerComponentImplementation(MusicPlugin.class);
                pico.registerComponentImplementation(MusicConfigImpl.class);
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

                MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);
                mainWindow.setUndecorated(true);
                mainWindow.setResizable(false);

                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
                device.setFullScreenWindow(mainWindow);
                mainWindow.setSize(device.getDisplayMode().getWidth(), device.getDisplayMode().getHeight());

//                mainWindow.setSize(800, 600);
//                mainWindow.setVisible(true);

                mainWindow.requestFocus();
            }
        });
    }

    private static void tweakSwing() {
        System.setProperty("swing.aatext", "true");
        UIManager.put("TabbedPane.tabsOpaque", "false");
        UIManager.put("TabbedPane.contentOpaque", "false");
    }
}
