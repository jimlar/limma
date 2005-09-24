package limma.plugins.music;

import limma.swing.SimpleListModel;


public class LinearPlayStrategy implements PlayStrategy {
    private SimpleListModel listModel;

    public LinearPlayStrategy(SimpleListModel listModel) {
        this.listModel = listModel;
    }

    public MusicFile getNextFileToPlay(MusicFile lastFile) {
        if (lastFile == null) {
            if (listModel.getSize() > 0) {
                return (MusicFile) listModel.getElementAt(0);
            } else {
                return null;
            }
        }
        int i = listModel.indexOf(lastFile);
        if (i < listModel.getSize() - 1) {
            MusicFile nextFile = (MusicFile) listModel.getElementAt(i + 1);
            return nextFile;
        }
        return null;
    }

    public String getName() {
        return "Linear";
    }
}
