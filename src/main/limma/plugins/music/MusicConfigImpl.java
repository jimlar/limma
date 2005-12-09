package limma.plugins.music;

import limma.AbstractConfiguration;

import java.io.File;

public class MusicConfigImpl extends AbstractConfiguration implements MusicConfig {

    public MusicConfigImpl() {
        super("music");
    }

    public File getMusicDir() {
        return getFile("musicdir");
    }
}
