package limma.plugins.music;

import limma.plugins.music.player.PlayerListener;

class LinearPlaylist implements PlayerListener {
    private MusicListModel listModel;
    private MusicPlugin plugin;

    public LinearPlaylist(MusicListModel listModel, MusicPlugin plugin) {
        this.listModel = listModel;
        this.plugin = plugin;
    }

    public void stopped(MusicFile musicFile) {
    }

    public void completed(MusicFile musicFile) {
        int i = listModel.indexOf(musicFile);
        if (i < listModel.getSize() - 1) {
            plugin.play((MusicFile) listModel.getElementAt(i + 1));
        } else {
            plugin.stop();
        }
    }
}
