package limma.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JDBCPersistenceManager implements PersistenceManager {
    private PersistenceConfig config;
    private Set<Class> persistentClasses = new HashSet<Class>();


    public JDBCPersistenceManager(PersistenceConfig config) {
        this.config = config;
    }

    public void addPersistentClass(Class clazz) {
        persistentClasses.add(clazz);
    }

    public void save(Object o) {
       
    }

    public Object create(Object o) {
        return null;
    }

    public void delete(Object o) {
    }

    public List loadAll(Class aClass) {
        return null;
    }
}
