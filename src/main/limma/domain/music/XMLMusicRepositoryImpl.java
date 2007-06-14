package limma.domain.music;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import limma.plugins.music.MusicConfig;
import limma.utils.AbstractXMLRepository;
import limma.utils.DirectoryScanner;

public class XMLMusicRepositoryImpl extends AbstractXMLRepository implements MusicRepository {
    private List<MusicFile> musicFiles;
    private MusicConfig musicConfig;


    public XMLMusicRepositoryImpl(MusicConfig musicConfig) {
        super(musicConfig.getCacheFile());
        this.musicConfig = musicConfig;
        addXmlAlias("music", MusicList.class);
        addXmlAlias("track", MusicFile.class);
        reReadMusicCache();
    }

    public List<MusicFile> getAll() {
        return musicFiles;
    }

    public void scanForMusic(ProgressListener progressListener) {
        int numDiskFiles = countDiskFiles();
        deleteRemovedOrOutdatedFiles(numDiskFiles, progressListener);
        addMissingFiles(numDiskFiles, progressListener);

        MusicList musicList = new MusicList();
        musicList.setTracks(musicFiles);
        store(musicList);

        reReadMusicCache();
    }

    private int countDiskFiles() {
        final int[] result = new int[]{0};
        DirectoryScanner scanner = new DirectoryScanner(musicConfig.getMusicDir(), true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isMusicFile(file)) {
                    result[0]++;
                }
                return true;
            }
        });
        return result[0];
    }

    private void deleteRemovedOrOutdatedFiles(int numDiskFiles, ProgressListener progressListener) {
        for (ListIterator i = musicFiles.listIterator(); i.hasNext();) {
            MusicFile musicFile = (MusicFile) i.next();
            File diskFile = musicFile.getFile();
            if (!diskFile.isFile() || diskFile.lastModified() != musicFile.getLastModified().getTime()) {
                i.remove();
                progressListener.progressUpdated(musicFiles.size() + numDiskFiles, i.nextIndex());
            }
        }
    }

    private void addMissingFiles(final int numDiskFiles, final ProgressListener progressListener) {
        final int numDatabaseFiles = musicFiles.size();
        final ArrayList<File> persistentFiles = new ArrayList<File>();
        for (Iterator i = musicFiles.iterator(); i.hasNext();) {
            MusicFile musicFile = (MusicFile) i.next();
            persistentFiles.add(musicFile.getFile());
        }

        final int[] filesScanned = new int[]{0};
        DirectoryScanner scanner = new DirectoryScanner(musicConfig.getMusicDir(), true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isMusicFile(file)) {
                    if (!persistentFiles.contains(file)) {
                        musicFiles.add(new MusicFile(file));
                    }
                    filesScanned[0]++;
                    progressListener.progressUpdated(numDatabaseFiles + numDiskFiles, filesScanned[0]);
                }
                return true;
            }
        });
    }

    private boolean isMusicFile(File file) {
        String name = file.getName().toLowerCase();
        return file.isFile() && (name.endsWith(".mp3") || name.endsWith(".flac") || name.endsWith(".ogg"));
    }


    private void reReadMusicCache() {
        MusicList musicList = (MusicList) load();
        if (musicList == null) {
            this.musicFiles = new ArrayList<MusicFile>();
        } else {
            this.musicFiles = new ArrayList<MusicFile>(musicList.getTracks());
        }
    }

    public static class MusicList {
        private List tracks;


        public List getTracks() {
            return tracks;
        }

        public void setTracks(List tracks) {
            this.tracks = tracks;
        }
    }
}
