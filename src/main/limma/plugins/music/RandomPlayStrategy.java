package limma.plugins.music;

import java.util.Random;

public class RandomPlayStrategy implements PlayStrategy {
    private MusicListModel musicListModel;
    private Random random;

    public RandomPlayStrategy(MusicListModel musicListModel) {
        this.musicListModel = musicListModel;
        random = new Random();
    }

    public MusicFile getNextFileToPlay(MusicFile lastFile) {
        if (musicListModel.getSize() > 0) {
            int i = random.nextInt(musicListModel.getSize());
            return (MusicFile) musicListModel.getElementAt(i);
        }
        return null;
    }

    public String getName() {
        return "Random";
    }
}
