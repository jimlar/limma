package limma.swing;

import limma.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class TransactionalTask implements Task {
    private PersistenceManager persistenceManager;

    protected TransactionalTask(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public final void run() {
        Session session = persistenceManager.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            runInTransaction(session);
            transaction.commit();
            transaction = null;
        } finally {
            if (transaction != null) {
                transaction.rollback();
            }
            session.close();
        }
    }

    public abstract void runInTransaction(Session session);
}
