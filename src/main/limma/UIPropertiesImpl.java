package limma;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;

public class UIPropertiesImpl implements UIProperties {
    private Font largeFond;
    private Font mediumFont;
    private Font smallFont;

    public UIPropertiesImpl() {
        largeFond = Font.decode("Verdana 40");
        mediumFont = Font.decode("Verdana 30");
        smallFont = Font.decode("Verdana bold 20");
    }

    public Font getLargeFont() {
        return largeFond;
    }

    public Font getMediumFont() {
        return mediumFont;
    }

    public Font getSmallFont() {
        return smallFont;
    }

    public float getMenuTransparency() {
        return 0.6f;
    }

    public float getHeaderTransparency() {
        return 0.6f;
    }

    public URL getBackgroundImage() {
        try {

//            return new URL("file:resources/backgrounds/Big_Tux_by_Dimitra25.jpg");
            return new URL("file:resources/backgrounds/Writhing_by_samiam14.jpg");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
