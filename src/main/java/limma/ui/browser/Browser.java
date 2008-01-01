package limma.ui.browser;

import limma.application.Command;
import limma.application.CommandConsumer;

public interface Browser extends CommandConsumer {

    void addCellRenderer(BrowserNodeRenderer renderer);

    void addNavigationListener(BrowserListener listener);

    boolean consume(Command command);
}
