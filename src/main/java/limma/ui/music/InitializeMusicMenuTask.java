package limma.ui.music;

import limma.application.PlayerManager;
import limma.application.music.MusicPlayer;
import limma.domain.music.MusicFile;
import limma.domain.music.MusicRepository;
import limma.ui.browser.model.SimpleBrowserNode;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

class InitializeMusicMenuTask implements Task {
    private SimpleBrowserNode musicNode;
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;
    private MusicRepository musicRepository;

    public InitializeMusicMenuTask(SimpleBrowserNode musicNode, MusicPlayer musicPlayer, PlayerManager playerManager, MusicRepository musicRepository) {
        this.musicNode = musicNode;
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
        this.musicRepository = musicRepository;
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Loading music database...");

        musicNode.removeAllChildren();

        for (MusicFile file : musicRepository.getAll()) {
            List<String> pathElements = Arrays.asList(StringUtils.split(file.getPath(), '/'));
            addNodes(file, musicNode, pathElements);
        }
    }

    private void addNodes(MusicFile file, SimpleBrowserNode parent, List<String> path) {
        if (path.size() > 1) {
            SimpleBrowserNode container = (SimpleBrowserNode) parent.getFirstChildWithTitle(path.get(0));
            if (container == null) {
                container = new TrackContainerNode(path.get(0), musicPlayer, playerManager);
                parent.add(container);
            }
            addNodes(file, container, path.subList(1, path.size()));

        } else {
            parent.add(new TrackNode(file.getTitle(), file, musicPlayer, playerManager));
        }
        parent.sortByTitle();
    }
}
