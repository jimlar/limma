package limma.domain.music;

import java.util.List;

import limma.persistence.PersistenceManager;

public class HibernateMusicRepositoryImpl implements MusicRepository {
    private PersistenceManager persistenceManager;

    public HibernateMusicRepositoryImpl(PersistenceManager persistenceManager) {
        persistenceManager.addPersistentClass(MusicFile.class);
        this.persistenceManager = persistenceManager;
    }

    public void add(MusicFile musicFile) {
        persistenceManager.create(musicFile);
    }

    public void remove(MusicFile musicFile) {
        persistenceManager.delete(musicFile);
    }

    public List<MusicFile> getAll() {
        return persistenceManager.loadAll(MusicFile.class);
    }
}
