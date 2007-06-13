package limma.domain.music;

import java.util.List;

public interface MusicRepository {
    void add(MusicFile musicFile);

    void remove(MusicFile musicFile);

    List<MusicFile> getAll();
}
