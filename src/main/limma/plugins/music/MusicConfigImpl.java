package limma.plugins.music;

import limma.AbstractConfiguration;
import limma.utils.ExternalCommand;

import java.io.File;

public class MusicConfigImpl extends AbstractConfiguration implements MusicConfig {

    public MusicConfigImpl() {
        super("music");
    }

    public File getMusicDir() {
        return getFile("musicdir");
    }

    public ExternalCommand getExternalPlayerCommand() {
        return new ExternalCommand(getString("externalplayer"));

    }
}
