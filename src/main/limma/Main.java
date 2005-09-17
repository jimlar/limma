package limma;

import java.awt.*;

public class Main {

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
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
