package limma.domain.music;

import java.util.List;

public interface MusicRepository {

    List<MusicFile> getAll();

    void scanForMusic(ProgressListener progressListener);
}
