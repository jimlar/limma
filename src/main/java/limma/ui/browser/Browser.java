package limma.ui.browser;

import limma.application.Command;
import limma.application.CommandConsumer;

public interface Browser extends CommandConsumer {

    void addCellRenderer(NavigationNodeRenderer renderer);

    void addNavigationListener(NavigationListener listener);

    boolean consume(Command command);
}
