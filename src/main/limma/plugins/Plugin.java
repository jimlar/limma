package limma.plugins;

import javax.swing.*;
import java.awt.event.KeyEvent;

public interface Plugin {
    String getPluginName();

    JComponent getPluginView();

    void pluginEntered();

    void keyPressed(KeyEvent e, PluginManager pluginManager);
}
