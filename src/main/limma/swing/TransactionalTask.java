package limma.swing;

import limma.persistence.PersistenceManager;

public abstract class TransactionalTask implements Task {
    private PersistenceManager persistenceManager;

    protected TransactionalTask(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public final void run(TaskFeedback feedback) {
            runInTransaction(feedback, persistenceManager);
    }

    public abstract void runInTransaction(TaskFeedback feedback, PersistenceManager persistenceManager);
}
