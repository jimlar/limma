package limma.plugins.music;

import java.io.File;

import limma.AbstractConfiguration;
import limma.utils.ExternalCommand;

public class MusicConfigImpl extends AbstractConfiguration implements MusicConfig {

    public MusicConfigImpl() {
        super("music");
    }

    public File getCacheFile() {
        return new File(getMusicDir(), "cache.xml");
    }

    public File getMusicDir() {
        return getFile("musicdir");
    }

    public ExternalCommand getMPlayerCommand() {
        return new ExternalCommand(getString("mplayer"));

    }
}
