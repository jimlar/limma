package limma.plugins.music;

import javax.swing.*;
import java.util.Collections;
import java.util.List;

class MusicListModel extends AbstractListModel {
    private List musicFiles = Collections.EMPTY_LIST;

    public int getSize() {
        return musicFiles.size();
    }

    public Object getElementAt(int index) {
        return musicFiles.get(index);
    }

    public void setMusicFiles(List musicFiles) {
        List oldFiles = this.musicFiles;
        this.musicFiles = musicFiles;
        if (oldFiles.size() > 0) {
            fireIntervalRemoved(this, 0, oldFiles.size() - 1);
        }
        if (musicFiles.size() > 0) {
            fireIntervalAdded(this, 0, musicFiles.size() - 1);
        }
    }

    public int indexOf(Object o) {
        return musicFiles.indexOf(o);
    }

    public void fireChanged(MusicFile file) {
        int i = indexOf(file);
        if (i != -1) {
            fireContentsChanged(this, i, i);
        }
    }
}
