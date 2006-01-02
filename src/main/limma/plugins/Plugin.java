package limma.plugins;

import limma.swing.navigationlist.NavigationModel;
import limma.PlayerManager;

import javax.swing.*;
import java.awt.event.KeyEvent;

public interface Plugin {
    String getPluginName();

    JComponent getPluginView();

    void pluginEntered();

    boolean keyPressed(KeyEvent e, PluginManager pluginManager);

    void init(NavigationModel navigationModel, PlayerManager playerManager);
}
