package limma.plugins.game;

import java.awt.Color;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import limma.plugins.Plugin;
import limma.plugins.PluginManager;
import limma.swing.AntialiasLabel;

public class GamePlugin implements Plugin {
    public String getPluginName() {
        return "game";
    }

    public JComponent getPluginComponent() {
        JPanel panel = new JPanel();
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Games");
        titledBorder.setTitleFont(AntialiasLabel.DEFAULT_FONT);
        titledBorder.setTitleColor(Color.white);
        panel.setBorder(titledBorder);
        panel.setOpaque(false);
        return panel;
    }

    public void pluginEntered() {
    }

    public void keyPressed(KeyEvent e, PluginManager pluginManager) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                pluginManager.exitPlugin();
                break;
        }
    }
}
