package limma.plugins.menu;

import limma.MainWindow;

public class PluginNode extends MenuNode {
    private String plugin;
    private MainWindow window;

    public PluginNode(String title, String plugin, MainWindow window) {
        super(title);
        this.plugin = plugin;
        this.window = window;
    }

    public void execute() {
        window.showPlugin(plugin);
    }
}
