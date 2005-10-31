package limma;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationImpl implements Configuration {
    private Properties properties;

    public ConfigurationImpl() throws IOException {
        properties = new Properties();
        FileInputStream in = null;
        try {
            in = new FileInputStream(new File(System.getProperty("user.home") + File.separator + ".limmarc"));
            properties.load(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public File getFile(String property) {
        return new File(getString(property));
    }

    public int getInt(String property) {
        try {
            return Integer.parseInt(getString(property));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The configuration property is not an integer: " + property);
        }
    }

    public String getString(String property) {
        String value = properties.getProperty(property);
        if (value == null) {
            throw new IllegalArgumentException("Configuration property missing: " + property);
        }
        return value;
    }

    public String[] getCommandLine(String property, String append) {
        return getCommandLine(property, new String[]{append});
    }

    public String[] getCommandLine(String commandProperty, String[] append) {
        String[] command = StringUtils.split(getString(commandProperty), ' ');
        String[] result = new String[command.length + append.length];
        System.arraycopy(command, 0, result, 0, command.length);
        System.arraycopy(append, 0, result, command.length, append.length);
        return result;
    }
}
