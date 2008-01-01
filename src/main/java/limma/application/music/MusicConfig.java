package limma.application.music;

import limma.utils.ExternalCommand;

import java.io.File;

public interface MusicConfig {

    File getCacheFile();

    File getMusicDir();

    ExternalCommand getMPlayerCommand();
}
