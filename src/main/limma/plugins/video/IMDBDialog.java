package limma.plugins.video;

import limma.swing.LimmaDialog;
import limma.swing.DialogManager;
import limma.swing.AntialiasLabel;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.InputMethodEvent;
import java.awt.*;

public class IMDBDialog extends LimmaDialog {
    private LimmaTextField textField;
    private Video video;

    public IMDBDialog(DialogManager dialogManager, Video video) {
        super(dialogManager);
        this.video = video;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new AntialiasLabel("IMDB Number:"));
        textField = new LimmaTextField();
        textField.setOpaque(true);
        textField.setColumns(10);
        textField.setFont(AntialiasLabel.DEFAULT_FONT);
        textField.setForeground(Color.white);
        panel.add(textField);
        add(panel);
    }

    public void open() {
        super.open();
        textField.requestFocusInWindow();
    }

    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                close();
                break;
            default:
                textField.processKeyEvent(e);
        }
    }

    private static class LimmaTextField extends JTextField {
        public void processKeyEvent(KeyEvent e) {
            super.processComponentKeyEvent(e);
        }
    }
}
