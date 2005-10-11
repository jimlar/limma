package limma.persistence;

import limma.plugins.music.MusicFile;

import java.util.List;

public interface PersistenceManager {

    void addPersistentClass(Class clazz);

    void create(Object o);

    void delete(Object o);

    List query(String queryName);

    List query(String queryName, String parameterName, Object parameterValue);

    Object querySingle(String queryName, String parameterName, Object parameterValue);
}
