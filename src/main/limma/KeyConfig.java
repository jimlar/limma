package limma;

import java.awt.event.KeyEvent;

public class KeyConfig {
    public Command getCommandForEvent(KeyEvent event) {
        switch (event.getKeyCode()) {

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
                return Command.LEFT;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
                return Command.RIGHT;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
                return Command.UP;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
                return Command.DOWN;

            case KeyEvent.VK_ENTER:
                return Command.ACTION;

            case KeyEvent.VK_ESCAPE:
                return Command.EXIT;

            case KeyEvent.VK_M:
                return Command.MENU;

            case KeyEvent.VK_S:
                return Command.STOP;

            case KeyEvent.VK_N:
                return Command.NEXT;

            case KeyEvent.VK_P:
                return Command.PREVIOUS;

            case KeyEvent.VK_F:
                return Command.FF;

            case KeyEvent.VK_R:
                return Command.REW;

            case KeyEvent.VK_SPACE:
                return Command.PAUSE;
        }
        return Command.NOTHING;
    }
}