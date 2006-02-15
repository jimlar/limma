package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.Task;
import limma.swing.TransactionalTask;
import limma.swing.navigationlist.MenuNode;
import limma.UIProperties;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

class LoadVideosTask implements Task {
    private PersistenceManager persistenceManager;
    private MenuNode titlesNode;
    private MenuNode tagsNode;
    private VideoPlayer videoPlayer;
    private DialogManager dialogManager;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private UIProperties uiProperties;

    public LoadVideosTask(PersistenceManager persistenceManager, MenuNode titlesNode, MenuNode categoriesNode, VideoPlayer videoPlayer, DialogManager dialogManager, IMDBSevice imdbSevice, VideoConfig videoConfig, UIProperties uiProperties) {
        this.persistenceManager = persistenceManager;
        this.titlesNode = titlesNode;
        this.tagsNode = categoriesNode;
        this.videoPlayer = videoPlayer;
        this.dialogManager = dialogManager;
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.uiProperties = uiProperties;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading movies...", uiProperties);
    }

    public void run() {
        titlesNode.removeAllChildren();
        tagsNode.removeAllChildren();

        Collection<String> tags = videoConfig.getTags();
        for (Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            MenuNode tagNode = new MenuNode(tag);
            tagsNode.add(tagNode);
        }
        tagsNode.sortByTitle();
        MenuNode untaggedNode = new MenuNode("<Untagged>");
        tagsNode.add(untaggedNode);

        List videos = persistenceManager.query("all_videos");

        for (Iterator i = videos.iterator(); i.hasNext();) {
            final Video video = (Video) i.next();
            addMovieNode(video, titlesNode);
            if (video.getTags().isEmpty()) {
                addMovieNode(video, untaggedNode);
            } else {
                for (Iterator j = video.getTags().iterator(); j.hasNext();) {
                    String tag = (String) j.next();
                    addMovieNode(video, tagsNode.getFirstChildWithTitle(tag));
                }
            }
        }
        titlesNode.sortByTitle();
        untaggedNode.sortByTitle();
    }

    private void addMovieNode(Video video, MenuNode node) {
        MovieMenuNode movieNode = new MovieMenuNode(video, videoPlayer, dialogManager, uiProperties);
        MenuNode selectTagsNode = new MenuNode("Select Tags");

        for (Iterator<String> i = videoConfig.getTags().iterator(); i.hasNext();) {
            String tag = i.next();
            selectTagsNode.add(new ToggleTagNode(tag, video));
        }

        movieNode.add(selectTagsNode);
        movieNode.add(new UpdateFromIMDBNode(video));
        node.add(movieNode);
    }

    private class ToggleTagNode extends MenuNode {
        private Video video;

        public ToggleTagNode(String tag, Video video) {
            super(tag);
            this.video = video;
        }

        public void performAction() {
            dialogManager.executeInDialog(new TransactionalTask(persistenceManager) {
                public void runInTransaction(Session session) {
                    if (video.getTags().contains(tagsNode)) {
                        video.getTags().remove(getTitle());
                    } else {
                        video.getTags().add(getTitle());
                    }
                    session.update(video);
                }

                public JComponent createComponent() {
                    return new AntialiasLabel("Toggling tag", uiProperties);
                }
            });
        }
    }

    private class UpdateFromIMDBNode extends MenuNode {
        private final Video video;

        public UpdateFromIMDBNode(Video video) {
            super("Update from IMDB");
            this.video = video;
        }

        public void performAction() {
            IMDBDialog imdbDialog = new IMDBDialog(dialogManager, video, imdbSevice, persistenceManager, videoConfig, uiProperties);
            imdbDialog.open();
        }
    }
}
