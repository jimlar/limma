package limma.swing;

import limma.UIProperties;

import javax.swing.*;
import java.awt.*;

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
