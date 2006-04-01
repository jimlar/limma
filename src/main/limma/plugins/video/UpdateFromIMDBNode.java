package limma.plugins.video;

import limma.swing.DialogManager;
import limma.swing.menu.NavigationNode;

class UpdateFromIMDBNode extends NavigationNode {
    private final Video video;

    public UpdateFromIMDBNode(Video video) {
        this.video = video;
    }

    public String getTitle() {
        return "Update from IMDB";
    }

    public void performAction(DialogManager dialogManager) {
        IMDBDialog imdbDialog = (IMDBDialog) dialogManager.createAndOpen(IMDBDialog.class);
        imdbDialog.setVideo(video);
    }
}
