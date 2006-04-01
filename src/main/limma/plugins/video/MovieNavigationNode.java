package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.DialogManager;
import limma.swing.menu.NavigationNode;
import limma.swing.menu.SimpleNavigationNode;

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
        add(new EditMovieNode(video));
        add(new UpdateFromIMDBNode(video));
//        add(createEditTagsNode(video));
        add(createFilesNode(video));
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

    public NavigationNode createEditTagsNode(Video video) {
        SimpleNavigationNode editTagsNode = new SimpleNavigationNode("Tags");

        List<String> tags = movieStorage.getTags();
        for (Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            editTagsNode.add(new ToggleTagNode(tag, video, persistenceManager));
        }

        return editTagsNode;
    }


    public NavigationNode createFilesNode(Video video) {
        SimpleNavigationNode filesNode = new SimpleNavigationNode("Files");

        Set files = video.getFiles();
        for (Iterator i = files.iterator(); i.hasNext();) {
            final VideoFile file = (VideoFile) i.next();
            filesNode.add(new SimpleNavigationNode(file.getName()) {
                public void performAction(DialogManager dialogManager) {
                    player.play(file);
                }
            });
        }

        return filesNode;
    }
}
