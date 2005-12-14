package limma;

import limma.persistence.PersistenceConfigImpl;
import limma.persistence.PersistenceManagerImpl;
import limma.plugins.game.GameConfigImpl;
import limma.plugins.game.GamePlugin;
import limma.plugins.music.MusicConfigImpl;
import limma.plugins.music.MusicPlugin;
import limma.plugins.music.player.JavaSoundMusicPlayer;
import limma.plugins.music.player.ExternalMusicPlayer;
import limma.plugins.video.IMDBSeviceImpl;
import limma.plugins.video.VideoConfigImpl;
import limma.plugins.video.VideoPlugin;
import limma.swing.CursorHider;
import limma.swing.DialogManagerImpl;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        tweakSwing();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

                DefaultPicoContainer pico = new DefaultPicoContainer();
                pico.registerComponentImplementation(VideoPlugin.class);
                pico.registerComponentImplementation(VideoConfigImpl.class);
                pico.registerComponentImplementation(MusicPlugin.class);
                pico.registerComponentImplementation(MusicConfigImpl.class);
                pico.registerComponentImplementation(ExternalMusicPlayer.class);
                pico.registerComponentImplementation(GamePlugin.class);
                pico.registerComponentImplementation(GameConfigImpl.class);
                pico.registerComponentImplementation(MainWindow.class);
                pico.registerComponentImplementation(DialogManagerImpl.class);
                pico.registerComponentImplementation(PersistenceManagerImpl.class);
                pico.registerComponentImplementation(PersistenceConfigImpl.class);
                pico.registerComponentImplementation(IMDBSeviceImpl.class);
                pico.registerComponentImplementation(CursorHider.class);
                pico.registerComponentInstance(device);

                pico.start();

                MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);
                mainWindow.setUndecorated(true);
                mainWindow.setResizable(false);
                device.setFullScreenWindow(mainWindow);
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
