package limma.persistence;

import java.util.List;

public interface PersistenceManager {

    void addPersistentClass(Class clazz);

    void save(Object o);

    Object create(Object o);

    void delete(Object o);

    List loadAll(Class aClass);
}
