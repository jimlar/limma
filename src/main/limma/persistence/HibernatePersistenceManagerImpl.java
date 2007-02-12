package limma.persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.util.List;

public class HibernatePersistenceManagerImpl implements PersistenceManager {
    private Configuration configuration;
    private SessionFactory sessionFactory;

    public HibernatePersistenceManagerImpl(PersistenceConfig persistenceConfig) throws IOException {
        configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", persistenceConfig.getDriver());
        configuration.setProperty("hibernate.connection.url", persistenceConfig.getUrl());

        configuration.setProperty("hibernate.connection.username", persistenceConfig.getUsername());
        configuration.setProperty("hibernate.connection.password", persistenceConfig.getPassword());

        configuration.setProperty("hibernate.c3p0.min_size", "5");
        configuration.setProperty("hibernate.c3p0.max_size", "20");
        configuration.setProperty("hibernate.c3p0.timeout", "1800");
        configuration.setProperty("hibernate.c3p0.max_statements", "50");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");

        configuration.getProperties().store(System.out, "Using persistence properties");

//        configuration.setProperty("hibernate.show_sql", "true");
    }

    public void addPersistentClass(Class clazz) {
        configuration.addClass(clazz);
    }

    public List loadAll(final Class aClass) {
        return (List) withSession(new SessionTask() {
            public Object execute(Session session) {
                return session.createCriteria(aClass).list();
            }
        });
    }


    public Object create(final Object o) {
        Object key = withSession(new SessionTask() {
            public Object execute(Session session) {
                return session.save(o);
            }
        });
        return load(o.getClass(), (Long) key);
    }

    public void delete(final Object o) {
        withSession(new SessionTask() {
            public Object execute(Session session) {
                session.delete(o);
                return null;
            }
        });
    }

    public void save(final Object o) {
        withSession(new SessionTask() {
            public Object execute(Session session) {
                return session.merge(o);
            }
        });
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

    private Object load(final Class persistentClass, final long id) {
        return withSession(new SessionTask() {
            public Object execute(Session session) {
                return session.get(persistentClass, id);
            }
        });
    }

    private synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = configuration.buildSessionFactory();
        }
        return sessionFactory;
    }
}
