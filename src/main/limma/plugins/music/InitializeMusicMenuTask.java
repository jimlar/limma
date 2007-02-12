package limma.plugins.music;

import limma.PlayerManager;
import limma.persistence.PersistenceManager;
import limma.swing.Task;
import limma.swing.TaskFeedback;
import limma.swing.navigation.NavigationNode;
import limma.swing.navigation.SimpleNavigationNode;

import javax.swing.*;
import java.util.Iterator;

class InitializeMusicMenuTask implements Task {
    private PersistenceManager persistenceManager;
    private SimpleNavigationNode musicNode;
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public InitializeMusicMenuTask(PersistenceManager persistenceManager, SimpleNavigationNode musicNode, MusicPlayer musicPlayer, PlayerManager playerManager) {
        this.persistenceManager = persistenceManager;
        this.musicNode = musicNode;
        this.musicPlayer = musicPlayer;
        this.playerManager = playerManager;
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Loading music database...");
        final TrackContainerNode artistsNode = new TrackContainerNode("Artists", musicPlayer, playerManager);
        final TrackContainerNode albumsNode = new TrackContainerNode("Albums", musicPlayer, playerManager);
        final TrackContainerNode songsNode = new TrackContainerNode("Songs", musicPlayer, playerManager);

        java.util.List musicFiles = persistenceManager.loadAll(MusicFile.class);

        for (Iterator i = musicFiles.iterator(); i.hasNext();) {
            MusicFile file = (MusicFile) i.next();
            songsNode.add(new TrackNode(file.getArtist() + ": " + file.getTitle(), file, musicPlayer, playerManager));

            addToArtistsNode(artistsNode, file);
            addToAlbumsNode(albumsNode, file);
        }

        for (Iterator<NavigationNode> i = artistsNode.getChildren().iterator(); i.hasNext();) {
            SimpleNavigationNode artistNode = (SimpleNavigationNode) i.next();
            artistNode.sortByTitle();
        }

        artistsNode.sortByTitle();
        albumsNode.sortByTitle();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                musicNode.removeAllChildren();
                musicNode.add(artistsNode);
                musicNode.add(albumsNode);
                musicNode.add(songsNode);
            }
        });
    }

    private void addToArtistsNode(SimpleNavigationNode artistsNode, MusicFile file) {
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

    private void addToAlbumsNode(SimpleNavigationNode albumsNode, MusicFile file) {
        String albumName = file.getArtist() + ": " + file.getAlbum();
        TrackContainerNode albumNode = (TrackContainerNode) albumsNode.getFirstChildWithTitle(albumName);
        if (albumNode == null) {
            albumNode = new TrackContainerNode(albumName, musicPlayer, playerManager);
            albumsNode.add(albumNode);
        }
        albumNode.add(new TrackNode(file.getTitle(), file, musicPlayer, playerManager));
    }
}
