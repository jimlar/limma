package limma.plugins.video;

import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.swing.DialogManager;
import limma.swing.menu.NavigationNode;
import limma.swing.menu.SimpleNavigationNode;

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

    public MovieNavigationNode createMovieNode(Video video) {
        return new MovieNavigationNode(video, videoPlayer, dialogManager, uiProperties, this);
    }

    public NavigationNode createUpdateFromIMDBNode(Video video) {
        return new UpdateFromIMDBNode(video, dialogManager, imdbService, persistenceManager, videoConfig, uiProperties);
    }

    public NavigationNode createEditTagsNode(Video video) {
        SimpleNavigationNode editTagsNode = new SimpleNavigationNode("Edit Tags");

        List<String> tags = movieStorage.getTags();
        for (Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            editTagsNode.add(new ToggleTagNode(tag, video, dialogManager, persistenceManager, uiProperties));
        }

        return editTagsNode;
    }
}
