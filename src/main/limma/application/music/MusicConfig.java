package limma.application.music;

import java.io.File;

import limma.utils.ExternalCommand;

public interface MusicConfig {

    File getCacheFile();

    File getMusicDir();

    ExternalCommand getMPlayerCommand();
}
