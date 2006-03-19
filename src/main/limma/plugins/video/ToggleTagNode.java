package limma.plugins.video;

import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.TaskInfo;
import limma.swing.TransactionalTask;
import limma.swing.menu.NavigationNode;
import org.hibernate.Session;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

class ToggleTagNode extends NavigationNode {
    private String tag;
    private Video video;
    private DialogManager dialogManager;
    private PersistenceManager persistenceManager;
    private UIProperties uiProperties;

    public ToggleTagNode(String tag, Video video, DialogManager dialogManager, PersistenceManager persistenceManager, UIProperties uiProperties) {
        this.uiProperties = uiProperties;
        this.persistenceManager = persistenceManager;
        this.dialogManager = dialogManager;
        this.tag = tag;
        this.video = video;
    }

    public String getTitle() {
        return (video.getTags().contains(tag) ? "+ " : "- ") + tag;
    }

    public List<NavigationNode> getChildren() {
        return Collections.emptyList();
    }

    public void performAction() {
        dialogManager.executeInDialog(new TransactionalTask(persistenceManager) {
            public void runInTransaction(Session session) {
                if (video.getTags().contains(tag)) {
                    video.getTags().remove(tag);
                } else {
                    video.getTags().add(tag);
                }
                session.update(video);
            }

            public JComponent prepareToRun(TaskInfo taskInfo) {
                return new AntialiasLabel("Toggling tag", uiProperties);
            }
        });
    }
}
