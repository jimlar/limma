package limma.ui.video;

import limma.domain.video.Video;
import limma.ui.browser.MenuItem;
import limma.ui.dialogs.DialogManager;

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
