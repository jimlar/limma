package limma.persistence;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class PersistenceManagerImpl implements PersistenceManager {
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public PersistenceManagerImpl(PersistenceConfig persistenceConfig) {
        configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");


        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://" + persistenceConfig.getHost()
                                                              + "/" + persistenceConfig.getDatabase());
        configuration.setProperty("hibernate.connection.username", persistenceConfig.getUsername());
        configuration.setProperty("hibernate.connection.password", persistenceConfig.getPassword());
        configuration.setProperty("hibernate.connection.pool_size", "10");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
//        configuration.setProperty("hibernate.show_sql", "true");
    }

    public void addPersistentClass(Class clazz) {
        configuration.addClass(clazz);
    }

    private synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }

    public List query(final String queryName) {
        return (List) withSession(new SessionTask() {
            public Object execute(Session session) {
                return session.getNamedQuery(queryName).list();
            }
        });
    }

    public List query(final String queryName, final String parameterName, final Object parameterValue) {
        return (List) withSession(new SessionTask() {
            public Object execute(Session session) {
                Query namedQuery = session.getNamedQuery(queryName);
                namedQuery.setParameter(parameterName, parameterValue);
                return namedQuery.list();
            }
        });
    }

    public Object querySingle(String queryName, String parameterName, Object parameterValue) {
        List list = query(queryName, parameterName, parameterValue);
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("Query returned more than one object: " + queryName);
        }
        return list.get(0);
    }

    public Session openSession() {
        return getSessionFactory().openSession();
    }

    private Object withSession(SessionTask task) {
        SessionFactory sessionFactory = getSessionFactory();
        Session session = null;
        try {
            session = sessionFactory.openSession();
            return task.execute(session);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static interface SessionTask {
        Object execute(Session session);
    }
}
