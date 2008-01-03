package limma.application.music;

import limma.application.AbstractConfiguration;
import limma.domain.music.MusicFile;
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

    public File getDiskFile(MusicFile musicFile) {
        return new File(getMusicDir(), musicFile.getPath());
    }

    public String getPathRelativeToMusicDir(File file) {
        if (!file.getAbsolutePath().startsWith(getMusicDir().getAbsolutePath())) {
            throw new IllegalArgumentException("This file is not from the music dir");
        }
        return file.getAbsolutePath().substring(getMusicDir().getAbsolutePath().length() + 1);
    }
}
