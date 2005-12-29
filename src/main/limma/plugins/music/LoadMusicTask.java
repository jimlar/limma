package limma.plugins.music;

import limma.swing.Task;
import limma.swing.AntialiasLabel;
import limma.swing.navigationlist.DefaultNavigationNode;
import limma.persistence.PersistenceManager;
import limma.plugins.music.player.MusicPlayer;

import javax.swing.*;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;

class LoadMusicTask implements Task {
    private PersistenceManager persistenceManager;
    private DefaultNavigationNode musicNode;
    private MusicPlayer musicPlayer;

    public LoadMusicTask(PersistenceManager persistenceManager, DefaultNavigationNode musicNode, MusicPlayer musicPlayer) {
        this.persistenceManager = persistenceManager;
        this.musicNode = musicNode;
        this.musicPlayer = musicPlayer;
    }

    public JComponent createComponent() {
        return new AntialiasLabel("Loading music database...");
    }

    public void run() {
        final DefaultNavigationNode artistsNode = new DefaultNavigationNode("Artists");
        final DefaultNavigationNode albumsNode = new DefaultNavigationNode("Albums");
        final DefaultNavigationNode songsNode = new DefaultNavigationNode("Songs");

        java.util.List musicFiles = persistenceManager.query("all_musicfiles");

        for (Iterator i = musicFiles.iterator(); i.hasNext();) {
            MusicFile file = (MusicFile) i.next();
            songsNode.add(new TrackNode(file.getArtist() + ": " + file.getTitle(), file, musicPlayer));

            addToArtistsNode(artistsNode, file, musicPlayer);
            addToAlbumsNode(albumsNode, file, musicPlayer);
        }


        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                musicNode.removeAllChildren();
                musicNode.add(artistsNode);
                musicNode.add(albumsNode);
                musicNode.add(songsNode);
            }
        });
    }

    private void addToArtistsNode(DefaultNavigationNode artistsNode, MusicFile file, MusicPlayer musicPlayer) {
        TrackContainerNode artistNode = (TrackContainerNode) artistsNode.getFirstChildWithTitle(file.getArtist());
        if (artistNode == null) {
            artistNode = new TrackContainerNode(file.getArtist(), musicPlayer);
            artistsNode.add(artistNode);
        }

        TrackContainerNode albumNode = (TrackContainerNode) artistNode.getFirstChildWithTitle(file.getAlbum());
        if (albumNode == null) {
            albumNode = new TrackContainerNode(file.getAlbum(), musicPlayer);
            artistNode.add(albumNode);
        }
        albumNode.add(new TrackNode(file.getTitle(), file, musicPlayer));
    }

    private void addToAlbumsNode(DefaultNavigationNode albumsNode, MusicFile file, MusicPlayer musicPlayer) {
        String albumName = file.getArtist() + ": " + file.getAlbum();
        TrackContainerNode albumNode = (TrackContainerNode) albumsNode.getFirstChildWithTitle(albumName);
        if (albumNode == null) {
            albumNode = new TrackContainerNode(albumName, musicPlayer);
            albumsNode.add(albumNode);
        }
        albumNode.add(new TrackNode(file.getTitle(), file, musicPlayer));
    }


    private static class TrackNode extends TrackContainerNode {
        private MusicFile musicFile;

        public TrackNode(String title, MusicFile musicFile, MusicPlayer musicPlayer) {
            super(title, musicPlayer);
            this.musicFile = musicFile;
        }

        protected List<MusicFile> collectTracks() {
            return Collections.singletonList(musicFile);
        }
    }

    private static class TrackContainerNode extends DefaultNavigationNode {
        private MusicPlayer musicPlayer;

        public TrackContainerNode(String title, MusicPlayer musicPlayer) {
            super(title);
            this.musicPlayer = musicPlayer;
        }

        public void performAction() {
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
