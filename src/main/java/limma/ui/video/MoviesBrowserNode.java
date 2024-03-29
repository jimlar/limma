package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;

import java.util.Set;

public class MoviesBrowserNode extends SimpleBrowserNode {
    private VideoPlayer videoPlayer;
    private VideoRepository videoRepository;
    private PlayDVDDiscNode dvdNode;

    public MoviesBrowserNode(VideoPlayer videoPlayer, VideoRepository videoRepository, PlayDVDDiscNode dvdNode) {
        super("Movies");
        this.videoPlayer = videoPlayer;
        this.videoRepository = videoRepository;
        this.dvdNode = dvdNode;
        rebuild();
    }

    public void rebuild() {
        removeAllChildren();

        add(new AllMoviesNode(videoPlayer, videoRepository));
        add(createGenresNode());
        add(createDirectorsNode());
        add(createActorsNode());
        add(new NewMoviesNode(7, videoPlayer, videoRepository, "Last Weeks Movies"));
        add(new NewMoviesNode(30, videoPlayer, videoRepository, "Last Months Movies"));

        add(createTagsNode());

        add(dvdNode);
    }

    private BrowserModelNode createDirectorsNode() {
        SimpleBrowserNode directorsNode = new SimpleBrowserNode("Directors");
        Set<String> directors = videoRepository.getAllDirectors();
        for (String director : directors) {
            directorsNode.add(new DirectorNode(director, videoPlayer, videoRepository));
        }
        directorsNode.sort();
        return directorsNode;
    }

    private SimpleBrowserNode createTagsNode() {
        SimpleBrowserNode tagsNode = new SimpleBrowserNode("Tags");
        for (String tag : videoRepository.getTags()) {
            tagsNode.add(new MovieTagNode(tag, videoPlayer, videoRepository));
        }
        return tagsNode;
    }

    private SimpleBrowserNode createGenresNode() {
        SimpleBrowserNode genresNode = new SimpleBrowserNode("Genres");
        Set<String> genres = videoRepository.getAllGenres();
        for (String genre : genres) {
            genresNode.add(new GenreNode(genre, videoPlayer, videoRepository));
        }
        genresNode.sort();
        return genresNode;
    }

    private SimpleBrowserNode createActorsNode() {
        SimpleBrowserNode actorsNode = new SimpleBrowserNode("Actors");
        Set<String> actors = videoRepository.getAllActors();
        for (String actor : actors) {
            actorsNode.add(new ActorNode(actor, videoPlayer, videoRepository));
        }
        actorsNode.sort();
        return actorsNode;
    }
}
