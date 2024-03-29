package limma.application;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public abstract class AbstractConfiguration {
    private static final File CONFIG_FILE = new File("config", "limma.properties");
    private Properties properties;
    private String prefix;

    public AbstractConfiguration(String prefix) {
        this.prefix = prefix;
        properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(CONFIG_FILE);
            properties.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Cant load config file: " + CONFIG_FILE, e);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    protected File getFile(String property) {
        return new File(getString(property));
    }

    protected int getInt(String property) {
        try {
            return Integer.parseInt(getString(property));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The configuration property is not an integer: " + property);
        }
    }

    protected String getString(String property) {
        if (prefix != null) {
            property = prefix + "." + property;
        }
        String value = properties.getProperty(property);
        if (value == null) {
            throw new IllegalArgumentException("Configuration property missing: " + property);
        }
        return value;
    }
}
