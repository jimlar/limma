package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.DialogManager;
import limma.swing.navigation.NavigationNode;
import limma.swing.navigation.SimpleNavigationNode;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MovieNavigationNode extends SimpleNavigationNode {
    private Video video;
    private VideoPlayer player;
    private MovieStorage movieStorage;
    private PersistenceManager persistenceManager;

    public MovieNavigationNode(Video video, VideoPlayer player, MovieStorage movieStorage, PersistenceManager persistenceManager) {
        super(video.getTitle());
        this.persistenceManager = persistenceManager;
        this.movieStorage = movieStorage;
        this.video = video;
        this.player = player;
        add(new EditMovieMenuItem(video));
        add(new UpdateFromIMDBMenuItem(video));

//        add(createEditTagsNode(video));
        addFileNodes(video);
        sortByTitle();
    }

    public Video getVideo() {
        return video;
    }

    public String getTitle() {
        return video.getTitle();
    }


    public void performAction(DialogManager dialogManager) {
        player.play(getVideo());
    }

    private NavigationNode createEditTagsNode(Video video) {
        SimpleNavigationNode editTagsNode = new SimpleNavigationNode("Tags");

        List<String> tags = movieStorage.getTags();
        for (Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            editTagsNode.add(new ToggleTagNode(tag, video, persistenceManager));
        }

        return editTagsNode;
    }


    private void addFileNodes(Video video) {
        Set files = video.getFiles();
        for (Iterator i = files.iterator(); i.hasNext();) {
            final VideoFile file = (VideoFile) i.next();
            add(new SimpleNavigationNode(file.getName()) {
                public void performAction(DialogManager dialogManager) {
                    player.play(file);
                }
            });
        }
    }
}
