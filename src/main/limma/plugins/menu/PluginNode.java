package limma.plugins.menu;

import limma.plugins.PluginManager;

public class PluginNode extends MenuNode {
    private String plugin;
    private PluginManager pluginManager;

    public PluginNode(String title, String plugin, PluginManager pluginManager) {
        super(title);
        this.plugin = plugin;
        this.pluginManager = pluginManager;
    }

    public void execute() {
        pluginManager.enterPlugin(plugin);
    }
}
