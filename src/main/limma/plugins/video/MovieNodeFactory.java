package limma.plugins.video;

import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.swing.DialogManager;
import limma.swing.menu.MenuNode;
import limma.swing.menu.SimpleMenuNode;

import java.util.Iterator;
import java.util.List;

public class MovieNodeFactory {
    private VideoPlayer videoPlayer;
    private DialogManager dialogManager;
    private UIProperties uiProperties;
    private IMDBService imdbService;
    private PersistenceManager persistenceManager;
    private MovieStorage movieStorage;
    private VideoConfig videoConfig;

    public MovieNodeFactory(MovieStorage movieStorage, VideoPlayer videoPlayer, DialogManager dialogManager, UIProperties uiProperties, IMDBService imdbService, PersistenceManager persistenceManager, VideoConfig videoConfig) {
        this.movieStorage = movieStorage;
        this.videoConfig = videoConfig;
        this.persistenceManager = persistenceManager;
        this.imdbService = imdbService;
        this.videoPlayer = videoPlayer;
        this.dialogManager = dialogManager;
        this.uiProperties = uiProperties;
    }

    public AllMoviesNode createAllMoviesNode() {
        return new AllMoviesNode(movieStorage, this);
    }

    public MovieTagNode createTagNode(String tag) {
        return new MovieTagNode(tag, movieStorage, this);
    }

    public MovieMenuNode createMovieNode(Video video) {
        return new MovieMenuNode(video, videoPlayer, dialogManager, uiProperties, this);
    }

    public MenuNode createUpdateFromIMDBNode(Video video) {
        return new UpdateFromIMDBNode(video, dialogManager, imdbService, persistenceManager, videoConfig, uiProperties);
    }

    public MenuNode createEditTagsNode(Video video) {
        SimpleMenuNode editTagsNode = new SimpleMenuNode("Edit Tags");

        List<String> tags = movieStorage.getTags();
        for (Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            editTagsNode.add(new ToggleTagNode(tag, video, dialogManager, persistenceManager, uiProperties));
        }

        return editTagsNode;
    }
}
