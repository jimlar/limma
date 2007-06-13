package limma.plugins.video;

import limma.domain.video.Video;
import limma.swing.DialogManager;
import limma.swing.navigation.MenuItem;

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
