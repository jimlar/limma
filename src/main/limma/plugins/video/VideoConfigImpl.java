package limma.plugins.video;

import limma.AbstractConfiguration;
import limma.utils.ExternalCommand;

import java.io.File;

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
}
