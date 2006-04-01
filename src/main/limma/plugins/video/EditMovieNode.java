package limma.plugins.video;

import limma.swing.DialogManager;
import limma.swing.menu.NavigationNode;

class EditMovieNode extends NavigationNode {
    private final Video video;

    public EditMovieNode(Video video) {
        this.video = video;
    }

    public String getTitle() {
        return "Edit";
    }

    public void performAction(DialogManager dialogManager) {
        EditMovieDialog dialog = (EditMovieDialog) dialogManager.createAndOpen(EditMovieDialog.class);
        dialog.setVideo(video);
    }
}
