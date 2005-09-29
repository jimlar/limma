package limma;

import java.awt.*;

import org.picocontainer.defaults.DefaultPicoContainer;
import limma.plugins.music.MusicPlugin;
import limma.plugins.game.GamePlugin;
import limma.plugins.Plugin;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(MusicPlugin.class);
        pico.registerComponentImplementation(GamePlugin.class);
        pico.registerComponentImplementation(MainWindow.class);
        pico.registerComponentImplementation(JDesktopPane.class);
        pico.registerComponentInstance(device);

        MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);
        mainWindow.addPlugins(pico.getComponentInstancesOfType(Plugin.class));
/*
        mainWindow.setVisible(true);
        mainWindow.setSize(300, 400);
        */
        mainWindow.setUndecorated(true);
        mainWindow.setResizable(false);
        device.setFullScreenWindow(mainWindow);
        mainWindow.requestFocus();
    }
}
