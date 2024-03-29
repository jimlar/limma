package limma.application.video;

import limma.utils.ExternalCommand;

import java.io.File;
import java.util.Collection;

public interface VideoConfig {
    File getMovieDir();

    File getPosterDir();

    int getSimilarFileDistance();

    String getDefaultPlayer();

    String getDvdPlayer();

    ExternalCommand getDvdPlayerCommand();

    ExternalCommand getDefaultPlayerCommand();

    Collection<String> getVideoFileExtensions();

    ExternalCommand getPlayDvdDiscCommand();

    File getMetaDataFile();
}
