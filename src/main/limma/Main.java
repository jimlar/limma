package limma;

import java.awt.*;

import org.picocontainer.defaults.DefaultPicoContainer;
import limma.plugins.music.MusicPlugin;
import limma.plugins.game.GamePlugin;

public class Main {

    public static void main(String[] args) {

        DefaultPicoContainer pico = new DefaultPicoContainer();
        pico.registerComponentImplementation(MusicPlugin.class);
        pico.registerComponentImplementation(GamePlugin.class);
        pico.registerComponentImplementation(MainWindow.class);

        MainWindow mainWindow = (MainWindow) pico.getComponentInstanceOfType(MainWindow.class);
/*
        mainWindow.setVisible(true);
        mainWindow.setSize(300, 400);
        */
        mainWindow.setUndecorated(true);
        mainWindow.setResizable(false);
        GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(mainWindow);
        mainWindow.requestFocus();
    }
}
