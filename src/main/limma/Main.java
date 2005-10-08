package limma;

import limma.plugins.Plugin;
import limma.persistence.PersistenceManagerImpl;
import limma.persistence.PersistenceManager;
import limma.plugins.video.VideoPlugin;
import limma.plugins.game.GamePlugin;
import limma.plugins.music.MusicPlugin;
import limma.swing.DialogManagerImpl;
import limma.persistence.PersistenceManager;
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
                pico.registerComponentImplementation(MusicPlugin.class);
                pico.registerComponentImplementation(GamePlugin.class);
                pico.registerComponentImplementation(MainWindow.class);
                pico.registerComponentImplementation(DialogManagerImpl.class);
                pico.registerComponentImplementation(PersistenceManagerImpl.class);
                pico.registerComponentInstance(device);

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
