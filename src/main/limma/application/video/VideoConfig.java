package limma.application.video;

import java.io.File;
import java.util.Collection;

import limma.utils.ExternalCommand;

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
