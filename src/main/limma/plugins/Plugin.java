package limma.plugins;

import javax.swing.*;
import java.awt.event.KeyEvent;

public interface Plugin {
    String getPluginName();

    JComponent getPluginComponent();

    void activatePlugin();

    /**
     * @return true if event was consumed
     */
    void keyPressed(KeyEvent e, PluginManager pluginManager);
}
