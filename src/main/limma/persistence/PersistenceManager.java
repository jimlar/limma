package limma.persistence;

import limma.plugins.music.MusicFile;

import java.util.List;

public interface PersistenceManager {

    void addPersistentClass(Class clazz);

    List loadAll(Class clazz);

    void deleteAll(Class clazz);

    void create(Object o);

    List query(String queryName, String parameterName, Object parameterValue);

    Object querySingle(String queryName, String parameterName, Object parameterValue);
}
