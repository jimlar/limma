package limma;

import limma.plugins.Plugin;
import limma.plugins.game.GamePlugin;
import limma.plugins.music.MusicPlugin;
import limma.swing.DialogManagerImpl;
import org.picocontainer.defaults.DefaultPicoContainer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        System.setProperty("swing.aatext", "true");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

                DefaultPicoContainer pico = new DefaultPicoContainer();
                pico.registerComponentImplementation(MusicPlugin.class);
                pico.registerComponentImplementation(GamePlugin.class);
                pico.registerComponentImplementation(MainWindow.class);
                pico.registerComponentImplementation(DialogManagerImpl.class);
                pico.registerComponentInstance(device);

                MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);
                mainWindow.addPlugins(pico.getComponentInstancesOfType(Plugin.class));
                mainWindow.setUndecorated(true);
                mainWindow.setResizable(false);
                device.setFullScreenWindow(mainWindow);
                mainWindow.requestFocus();
            }
        });
    }
}
