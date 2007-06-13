package limma.plugins.video;

import limma.domain.video.Video;
import limma.swing.DialogManager;
import limma.swing.navigation.MenuItem;

class UpdateFromIMDBMenuItem extends MenuItem {
    private final Video video;

    public UpdateFromIMDBMenuItem(Video video) {
        super("Update from IMDB");
        this.video = video;
    }

    public void performAction(DialogManager dialogManager) {
        IMDBDialog imdbDialog = (IMDBDialog) dialogManager.createAndOpen(IMDBDialog.class);
        imdbDialog.setVideo(video);
    }
}
