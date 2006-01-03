package limma;

import javax.swing.*;

public interface Player {

    JComponent getPlayerPane();

    void next();

    void previous();

    void ff();

    void rew();

    void pause();

    void stop();
}
