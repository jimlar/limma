package limma.application.video;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import limma.domain.video.Video;
import limma.domain.video.VideoFile;
import limma.domain.video.VideoRepository;
import limma.ui.browser.NavigationNode;
import limma.ui.browser.SimpleNavigationNode;
import limma.ui.dialogs.DialogManager;

public class MovieNavigationNode extends SimpleNavigationNode {
    private Video video;
    private VideoPlayer player;
    private VideoRepository videoRepository;

    public MovieNavigationNode(Video video, VideoPlayer player, VideoRepository videoRepository) {
        super(video.getTitle());
        this.video = video;
        this.player = player;
        this.videoRepository = videoRepository;
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

        List<String> tags = videoRepository.getTags();
        for (Iterator<String> i = tags.iterator(); i.hasNext();) {
            String tag = i.next();
            editTagsNode.add(new ToggleTagNode(tag, video, videoRepository));
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
