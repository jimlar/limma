package limma.plugins.video;

import limma.AbstractConfiguration;
import limma.utils.ExternalCommand;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class VideoConfigImpl extends AbstractConfiguration implements VideoConfig {

    public VideoConfigImpl() {
        super("video");
    }

    public File getMovieDir() {
        return getFile("moviedir");
    }

    public File getPosterDir() {
        return getFile("posterdir");
    }

    public int getSimilarFileDistance() {
        return getInt("similarfiledistance");
    }

    public String getDefaultPlayer() {
        return getString("command.default");
    }

    public String getDvdPlayer() {
        return getString("command.dvd");
    }

    public ExternalCommand getDvdPlayerCommand() {
        return new ExternalCommand(getString("command.dvd"));
    }

    public ExternalCommand getDefaultPlayerCommand() {
        return new ExternalCommand(getString("command.default"));
    }

    public Collection getVideoFileExtensions() {
        ArrayList result = new ArrayList();
        String[] extensions = StringUtils.split(getString("moviefileextensions"), ',');
        for (int i = 0; i < extensions.length; i++) {
            String extension = extensions[i];
            result.add(extension.toLowerCase().trim());
        }
        return result;
    }

    public ExternalCommand getPlayDvdDiscCommand() {
        return new ExternalCommand(getString("command.dvddisc"));
    }
}
