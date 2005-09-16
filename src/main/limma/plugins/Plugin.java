package limma.plugins;

import javax.swing.*;

public interface Plugin {
    String getPluginName();

    JComponent getPluginComponent();

    void activatePlugin();
}
