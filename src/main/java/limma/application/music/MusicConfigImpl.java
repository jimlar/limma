package limma.application.music;

import limma.application.AbstractConfiguration;
import limma.utils.ExternalCommand;

import java.io.File;

public class MusicConfigImpl extends AbstractConfiguration implements MusicConfig {

    public MusicConfigImpl() {
        super("music");
    }

    public File getCacheFile() {
        return new File(getMusicDir(), "cache.xml.gz");
    }

    public File getMusicDir() {
        return getFile("musicdir");
    }

    public ExternalCommand getMPlayerCommand() {
        return new ExternalCommand(getString("mplayer"));

    }
}
