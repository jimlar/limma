package limma.plugins.menu;

import limma.plugins.PluginManager;
import limma.swing.LimmaMenuItem;

public class PluginItem extends LimmaMenuItem {
    private String plugin;
    private PluginManager pluginManager;

    public PluginItem(String title, String plugin, PluginManager pluginManager) {
        super(title);
        this.plugin = plugin;
        this.pluginManager = pluginManager;
    }

    public void execute() {
        pluginManager.enterPlugin(plugin);
    }
}
