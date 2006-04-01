package limma.swing;

import limma.persistence.PersistenceManager;
import org.hibernate.Session;
import org.hibernate.Transaction;

public abstract class TransactionalTask implements Task {
    private PersistenceManager persistenceManager;

    protected TransactionalTask(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public final void run(TaskFeedback feedback) {
        Session session = persistenceManager.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            runInTransaction(feedback, session);
            transaction.commit();
            transaction = null;
        } finally {
            if (transaction != null) {
                transaction.rollback();
            }
            session.close();
        }
    }

    public abstract void runInTransaction(TaskFeedback feedback, Session session);
}
