package limma.plugins.music;

import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.persistence.PersistenceManager;
import limma.plugins.music.player.MusicPlayer;
import limma.PlayerManager;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

class LoadMusicTask implements Task {
    private PersistenceManager persistenceManager;
    private DefaultNavigationNode musicNode;
    private MusicPlayer musicPlayer;
    private PlayerManager playerManager;

    public LoadMusicTask(PersistenceManager persistenceManager, DefaultNavigationNode musicNode, MusicPlayer musicPlayer, PlayerManager playerManager) {
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


    private static class TrackNode extends TrackContainerNode {
        private MusicFile musicFile;

        public TrackNode(String title, MusicFile musicFile, MusicPlayer musicPlayer, PlayerManager playerManager) {
            super(title, musicPlayer, playerManager);
            this.musicFile = musicFile;
        }

        protected List<MusicFile> collectTracks() {
            return Collections.singletonList(musicFile);
        }
    }

    private static class TrackContainerNode extends DefaultNavigationNode {
        private MusicPlayer musicPlayer;
        private PlayerManager playerManager;

        public TrackContainerNode(String title, MusicPlayer musicPlayer, PlayerManager playerManager) {
            super(title);
            this.musicPlayer = musicPlayer;
            this.playerManager = playerManager;
        }

        public void performAction() {
            playerManager.switchTo(musicPlayer);
            musicPlayer.play(collectTracks());
        }

        protected java.util.List<MusicFile> collectTracks() {
            ArrayList<MusicFile> result = new ArrayList<MusicFile>();
            for (int i = 0; i < getChildCount(); i++) {
                TrackContainerNode child = (TrackContainerNode) getChildAt(i);
                result.addAll(child.collectTracks());
            }
            return result;
        }
    }
}
