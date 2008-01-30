package limma.ui.video;

import limma.application.video.VideoPlayer;
import limma.domain.video.Video;
import limma.domain.video.VideoFile;
import limma.domain.video.VideoRepository;
import limma.ui.browser.model.BrowserModelNode;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.DialogManager;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MovieBrowserNode extends SimpleBrowserNode {
    private Video video;
    private VideoPlayer player;
    private VideoRepository videoRepository;

    public MovieBrowserNode(Video video, VideoPlayer player, VideoRepository videoRepository) {
        super(video.getTitle());
        this.video = video;
        this.player = player;
        this.videoRepository = videoRepository;
        add(new EditMovieMenuItem(video));
        add(new UpdateFromIMDBMenuItem(video));

//        add(createEditTagsNode(video));
        addFileNodes(video);
        sort();
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

    private BrowserModelNode createEditTagsNode(Video video) {
        SimpleBrowserNode editTagsNode = new SimpleBrowserNode("Tags");

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
            add(new SimpleBrowserNode(file.getName()) {
                public void performAction(DialogManager dialogManager) {
                    player.play(file);
                }
            });
        }
    }
}
