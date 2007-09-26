package limma.ui;

import java.awt.Color;

import javax.swing.JLabel;

public class AntialiasLabel extends JLabel {

    public AntialiasLabel(UIProperties uiProperties) {
        this("", uiProperties);
    }

    public AntialiasLabel(String text, UIProperties uiProperties) {
        super(text);
        setFont(uiProperties.getSmallFont());
        setOpaque(false);
        setForeground(Color.white);
    }
}
