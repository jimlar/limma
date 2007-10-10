package limma.ui;

import limma.application.Command;

import java.awt.event.KeyEvent;

public class KeyConfig {
    public Command getCommandForEvent(KeyEvent event) {

        switch (event.getKeyCode()) {

            case KeyEvent.VK_LEFT:
            case KeyEvent.VK_KP_LEFT:
            case KeyEvent.VK_NUMPAD4:
                return Command.LEFT;

            case KeyEvent.VK_RIGHT:
            case KeyEvent.VK_KP_RIGHT:
            case KeyEvent.VK_NUMPAD6:
                return Command.RIGHT;

            case KeyEvent.VK_UP:
            case KeyEvent.VK_KP_UP:
            case KeyEvent.VK_NUMPAD8:
                return Command.UP;

            case KeyEvent.VK_DOWN:
            case KeyEvent.VK_KP_DOWN:
            case KeyEvent.VK_NUMPAD2:
                return Command.DOWN;

            case KeyEvent.VK_ENTER:
                return Command.ACTION;

            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_SEPARATOR:
            case KeyEvent.VK_DECIMAL:
                return Command.EXIT;

            case KeyEvent.VK_M:
            case KeyEvent.VK_MULTIPLY:
                return Command.MENU;

            case KeyEvent.VK_S:
            case KeyEvent.VK_INSERT:
            case KeyEvent.VK_NUMPAD0:
                return Command.STOP;

            case KeyEvent.VK_N:
            case KeyEvent.VK_ADD:
                return Command.NEXT;

            case KeyEvent.VK_P:
            case KeyEvent.VK_SUBTRACT:
                return Command.PREVIOUS;

            case KeyEvent.VK_F:
            case KeyEvent.VK_PAGE_DOWN:
                return Command.FF;

            case KeyEvent.VK_R:
            case KeyEvent.VK_PAGE_UP:
                return Command.REW;

            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_5:
            case KeyEvent.VK_BEGIN:
                return Command.PAUSE;
        }
        return Command.NOTHING;
    }
}