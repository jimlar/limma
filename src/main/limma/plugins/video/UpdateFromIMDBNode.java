package limma.plugins.video;

import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.swing.DialogManager;
import limma.swing.menu.MenuNode;

import java.util.Collections;
import java.util.List;

class UpdateFromIMDBNode extends MenuNode {
    private final Video video;
    private DialogManager dialogManager;
    private IMDBService imdbService;
    private PersistenceManager persistenceManager;
    private VideoConfig videoConfig;
    private UIProperties uiProperties;

    public UpdateFromIMDBNode(Video video, DialogManager dialogManager, IMDBService imdbService, PersistenceManager persistenceManager, VideoConfig videoConfig, UIProperties uiProperties) {
        this.uiProperties = uiProperties;
        this.videoConfig = videoConfig;
        this.persistenceManager = persistenceManager;
        this.imdbService = imdbService;
        this.dialogManager = dialogManager;
        this.video = video;
    }

    public String getTitle() {
        return "Update from IMDB";
    }

    public List<MenuNode> getChildren() {
        return Collections.emptyList();
    }

    public void performAction() {
        IMDBDialog imdbDialog = new IMDBDialog(dialogManager, video, imdbService, persistenceManager, videoConfig, uiProperties);
        imdbDialog.open();
    }
}
