package limma;

import java.awt.*;
import java.net.URL;
import java.net.MalformedURLException;

public class UIPropertiesImpl implements UIProperties {

    public Font getLargeFont() {
        return Font.decode("Verdana 40");
    }

    public Font getMediumFont() {
        return Font.decode("Verdana 30");
    }

    public Font getSmallFont() {
        return Font.decode("Verdana bold 20");
    }

    public float getMenuTransparency() {
        return 0.6f;
    }

    public float getHeaderTransparency() {
        return 0.6f;
    }

    public URL getBackDropImage() {
        try {
            return new URL("file:resources/backgrounds/Writhing_by_samiam14.jpg");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
