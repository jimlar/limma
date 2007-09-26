package limma.application.video;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import limma.AbstractConfiguration;
import limma.utils.ExternalCommand;
import org.apache.commons.lang.StringUtils;

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

    public Collection<String> getVideoFileExtensions() {
        ArrayList<String> result = new ArrayList<String>();
        String[] extensions = StringUtils.split(getString("moviefileextensions"), ',');
        for (int i = 0; i < extensions.length; i++) {
            result.add(extensions[i].toLowerCase().trim());
        }
        return result;
    }

    public ExternalCommand getPlayDvdDiscCommand() {
        return new ExternalCommand(getString("command.dvddisc"));
    }

    public File getMetaDataFile() {
        return new File(getMovieDir(), "metadata.xml.gz");
    }

    public Collection<String> getTags() {
        ArrayList<String> result = new ArrayList<String>();
        String[] tags = StringUtils.split(getString("tags"), ',');
        for (int i = 0; i < tags.length; i++) {
            result.add(tags[i].trim());
        }
        return result;
    }
}
