package limma.plugins.music;

import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.persistence.PersistenceManager;
import limma.PlayerManager;

import javax.swing.*;
import java.util.Iterator;

class InitializeMusicMenuTask implements Task {
    private PersistenceManager persistenceManager;
    private DefaultNavigationNode musicNode;
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public InitializeMusicMenuTask(PersistenceManager persistenceManager, DefaultNavigationNode musicNode, MusicPlayer musicPlayer, PlayerManager playerManager) {
        this.persistenceManager = persistenceManager;
        this.musicNode = musicNode;
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading music database...");
    }

    public void run() {
        final TrackContainerNode artistsNode = new TrackContainerNode("Artists", musicPlayer, playerManager);
        final TrackContainerNode albumsNode = new TrackContainerNode("Albums", musicPlayer, playerManager);
        final TrackContainerNode songsNode = new TrackContainerNode("Songs", musicPlayer, playerManager);

        java.util.List musicFiles = persistenceManager.query("all_musicfiles");

        for (Iterator i = musicFiles.iterator(); i.hasNext();) {
            MusicFile file = (MusicFile) i.next();
            songsNode.add(new TrackNode(file.getArtist() + ": " + file.getTitle(), file, musicPlayer, playerManager));

            addToArtistsNode(artistsNode, file);
            addToAlbumsNode(albumsNode, file);
        }

        for (int i = 0; i < artistsNode.getChildCount(); i++) {
            DefaultNavigationNode artistNode = (DefaultNavigationNode) artistsNode.getChildAt(i);
            artistNode.sortByTitle();
        }
        artistsNode.sortByTitle();
        albumsNode.sortByTitle();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                musicNode.add(0, songsNode);
                musicNode.add(0, albumsNode);
                musicNode.add(0, artistsNode);
            }
        });
    }

    private void addToArtistsNode(DefaultNavigationNode artistsNode, MusicFile file) {
        TrackContainerNode artistNode = (TrackContainerNode) artistsNode.getFirstChildWithTitle(file.getArtist());
        if (artistNode == null) {
            artistNode = new TrackContainerNode(file.getArtist(), musicPlayer, playerManager);
            artistsNode.add(artistNode);
        }

        TrackContainerNode albumNode = (TrackContainerNode) artistNode.getFirstChildWithTitle(file.getAlbum());
        if (albumNode == null) {
            albumNode = new TrackContainerNode(file.getAlbum(), musicPlayer, playerManager);
            artistNode.add(albumNode);
        }
        albumNode.add(new TrackNode(file.getTitle(), file, musicPlayer, playerManager));
    }

    private void addToAlbumsNode(DefaultNavigationNode albumsNode, MusicFile file) {
        String albumName = file.getArtist() + ": " + file.getAlbum();
        TrackContainerNode albumNode = (TrackContainerNode) albumsNode.getFirstChildWithTitle(albumName);
        if (albumNode == null) {
            albumNode = new TrackContainerNode(albumName, musicPlayer, playerManager);
            albumsNode.add(albumNode);
        }
        albumNode.add(new TrackNode(file.getTitle(), file, musicPlayer, playerManager));
    }
}
