package limma.ui;

import javax.swing.JComponent;

import limma.CommandConsumer;

public interface Player extends CommandConsumer {

    JComponent getPlayerPane();
}
