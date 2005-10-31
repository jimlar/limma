package limma;

import java.io.File;

public interface Configuration {
    File getFile(String property);

    int getInt(String property);

    String getString(String property);

    String[] getCommandLine(String property, String append);

    String[] getCommandLine(String commandProperty, String[] append);
}
