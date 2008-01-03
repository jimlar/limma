package limma.application.music;

import limma.domain.music.MusicFile;
import limma.utils.ExternalCommand;

import java.io.File;

public interface MusicConfig {

    File getCacheFile();

    File getMusicDir();

    ExternalCommand getMPlayerCommand();

    File getDiskFile(MusicFile musicFile);

    String getPathRelativeToMusicDir(File file);
}
