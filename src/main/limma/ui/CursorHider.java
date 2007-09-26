package limma.ui;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.picocontainer.Startable;

public class CursorHider implements Startable, Runnable {
    private JFrame frame;
    private boolean cursorHidden;
    private Cursor normalCursor;
    private Cursor hiddenCursor;
    private long lastShowTime;

    public CursorHider(JFrame frame) {
        this.frame = frame;
    }

    public void start() {
        normalCursor = frame.getCursor();
        hiddenCursor = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), new Point(0, 0), "invisible");
        frame.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                showCursor();
            }
        });
        new Thread(this).start();
    }

    public void stop() {
    }

    public void run() {
        showCursor();
        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            if (System.currentTimeMillis() - lastShowTime > 2 * 1000) {
                hideCursor();
            }
        }
    }

    private synchronized void hideCursor() {
        if (!cursorHidden) {
            frame.setCursor(hiddenCursor);
            cursorHidden = true;
        }
    }

    private synchronized void showCursor() {
        if (cursorHidden) {
            frame.setCursor(normalCursor);
            cursorHidden = false;
        }
        lastShowTime = System.currentTimeMillis();
    }
}
