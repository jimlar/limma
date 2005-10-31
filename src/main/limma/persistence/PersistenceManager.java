package limma.persistence;

import org.hibernate.Session;

import java.util.List;

public interface PersistenceManager {

    void addPersistentClass(Class clazz);

    List query(String queryName);

    List query(String queryName, String parameterName, Object parameterValue);

    Object querySingle(String queryName, String parameterName, Object parameterValue);

    Session openSession();
}
