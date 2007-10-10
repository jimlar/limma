package limma.ui.video;

import limma.domain.video.Video;
import limma.ui.browser.MenuItem;
import limma.ui.dialogs.DialogManager;

class EditMovieMenuItem extends MenuItem {
    private final Video video;

    public EditMovieMenuItem(Video video) {
        super("Edit");
        this.video = video;
    }

    public void performAction(DialogManager dialogManager) {
        EditMovieDialog dialog = (EditMovieDialog) dialogManager.createAndOpen(EditMovieDialog.class);
        dialog.setVideo(video);
    }
}
