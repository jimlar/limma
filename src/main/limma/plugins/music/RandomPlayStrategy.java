package limma.plugins.music;

import limma.swing.SimpleListModel;

import java.util.Random;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class RandomPlayStrategy extends AbstractPlayStrategy {
    private Random random;

    public RandomPlayStrategy(SimpleListModel musicListModel) {
        super(musicListModel);
        random = new Random();
    }

    public MusicFile getNextFileToPlay(MusicFile lastFile, boolean lockArtist, boolean lockAlbum) {
        List filteredMusicList = getFilteredMusicList(lastFile, lockArtist, lockAlbum);
        if (filteredMusicList.size() > 0) {
            int i = random.nextInt(filteredMusicList.size());
            return (MusicFile) filteredMusicList.get(i);
        }
        return null;
    }
}
