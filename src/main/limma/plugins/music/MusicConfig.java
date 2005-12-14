package limma.plugins.music;

import limma.utils.ExternalCommand;

import java.io.File;

public interface MusicConfig {

    File getMusicDir();

    ExternalCommand getExternalPlayerCommand();
}
