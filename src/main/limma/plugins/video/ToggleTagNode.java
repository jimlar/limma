package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.DialogManager;
import limma.swing.TaskFeedback;
import limma.swing.TransactionalTask;
import limma.swing.navigation.NavigationNode;
import org.hibernate.Session;

class ToggleTagNode extends NavigationNode {
    private String tag;
    private Video video;
    private PersistenceManager persistenceManager;

    public ToggleTagNode(String tag, Video video, PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
        this.tag = tag;
        this.video = video;
    }

    public String getTitle() {
        return (video.getTags().contains(tag) ? "+ " : "- ") + tag;
    }

    public void performAction(DialogManager dialogManager) {
        dialogManager.executeInDialog(new ToggleTagTask(persistenceManager, video, tag));
    }

    public static class ToggleTagTask extends TransactionalTask {
        private Video video;
        private String tag;

        public ToggleTagTask(PersistenceManager persistenceManager, Video video, String tag) {
            super(persistenceManager);
            this.video = video;
            this.tag = tag;
        }

        public void runInTransaction(TaskFeedback feedback, Session session) {
            feedback.setStatusMessage("Toggling tag");
            if (video.getTags().contains(tag)) {
                video.getTags().remove(tag);
            } else {
                video.getTags().add(tag);
            }
            session.update(video);
        }
    }
}
