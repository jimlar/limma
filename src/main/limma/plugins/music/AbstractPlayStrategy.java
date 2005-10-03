package limma.plugins.music;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.ArrayList;

import limma.swing.SimpleListModel;

public abstract class AbstractPlayStrategy implements PlayStrategy {
    private SimpleListModel musicListModel;

    public AbstractPlayStrategy(SimpleListModel musicListModel) {
        this.musicListModel = musicListModel;
    }

    protected List getFilteredMusicList(MusicFile lastFile, boolean lockArtist, boolean lockAlbum) {
        if (lastFile == null) {
            return musicListModel.getObjects();
        }
        return new ArrayList(CollectionUtils.select(musicListModel.getObjects(), new LockPredicate(lastFile.getArtist(), lastFile.getAlbum(), lockArtist, lockAlbum)));
    }

    private static class LockPredicate implements Predicate {
        private String artist;
        private String album;
        private boolean lockArtist;
        private boolean lockAlbum;

        public LockPredicate(String artist, String album, boolean lockArtist, boolean lockAlbum) {
            this.artist = artist;
            this.album = album;
            this.lockArtist = lockArtist;
            this.lockAlbum = lockAlbum;
        }

        public boolean evaluate(Object o) {
            MusicFile file = (MusicFile) o;
            if (lockArtist) {
                if (!file.getArtist().equalsIgnoreCase(artist)) {
                    return false;
                }
                if (lockAlbum) {
                    return file.getAlbum().equalsIgnoreCase(album);
                }
            }
            return true;
        }
    }
}
