package limma.plugins.music;

import limma.swing.SimpleListModel;

import java.util.List;


public class LinearPlayStrategy extends AbstractPlayStrategy {

    public LinearPlayStrategy(SimpleListModel listModel) {
        super(listModel);
    }

    public MusicFile getNextFileToPlay(MusicFile lastFile, boolean lockArtist, boolean lockAlbum) {
        List filteredMusicList = getFilteredMusicList(lastFile, lockArtist, lockAlbum);
        if (lastFile == null) {
            if (filteredMusicList.size() > 0) {
                return (MusicFile) filteredMusicList.get(0);
            } else {
                return null;
            }
        }
        int i = filteredMusicList.indexOf(lastFile);
        if (i < filteredMusicList.size() - 1) {
            return (MusicFile) filteredMusicList.get(i + 1);
        }
        if (filteredMusicList.size() > 0) {
            return (MusicFile) filteredMusicList.get(0);
        }
        return null;
    }

}
