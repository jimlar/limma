package limma.plugins.music;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;

class ScanMusicFilesTask implements Task {
    private MusicPlugin musicPlugin;
    private PersistenceManager persistenceManager;
    private File musicDir;
    private AntialiasLabel statusLabel;

    public ScanMusicFilesTask(MusicPlugin musicPlugin, PersistenceManager persistenceManager) {
        this.musicPlugin = musicPlugin;
        this.persistenceManager = persistenceManager;
        musicDir = new File("/media/music/");
        statusLabel = new AntialiasLabel("Scanning for music files in " + musicDir.getAbsolutePath());
    }

    public JComponent createComponent() {
        return statusLabel;
    }

    public void run() {
        updateMusicDatabase();
        musicPlugin.reloadFileList();
    }

    private int countDiskFiles() {
        final int[] result = new int[]{0};
        DirectoryScanner scanner = new DirectoryScanner(musicDir, true);
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

    private void updateMusicDatabase() {
        int numDiskFiles = countDiskFiles();
        List musicFiles = persistenceManager.query("all_musicfiles");
        deleteRemovedOrOutdatedFiles(musicFiles, numDiskFiles);
        addMissingFiles(musicFiles.size(), numDiskFiles);
    }

    private void deleteRemovedOrOutdatedFiles(List musicFiles, int numDiskFiles) {
        for (ListIterator i = musicFiles.listIterator(); i.hasNext();) {
            MusicFile musicFile = (MusicFile) i.next();
            File diskFile = musicFile.getFile();
            if (!diskFile.isFile() || diskFile.lastModified() != musicFile.getLastModified().getTime()) {
                persistenceManager.delete(musicFile);
                updateProgress(musicFiles.size() + numDiskFiles, i.nextIndex());
            }
        }
    }

    private void addMissingFiles(final int numDatabaseFiles, final int numDiskFiles) {
        final int[] filesScanned = new int[]{0};
        DirectoryScanner scanner = new DirectoryScanner(musicDir, true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isMusicFile(file)) {
                    if (persistenceManager.querySingle("musicfile_by_path", "path", file.getAbsolutePath()) == null) {
                        persistenceManager.create(new MusicFile(file));
                    }
                    filesScanned[0]++;
                    updateProgress(numDatabaseFiles + numDiskFiles, filesScanned[0]);
                }
                return true;
            }
        });
    }

    private void updateProgress(int total, int completed) {
        if (total == 0) {
            return;
        }
        final int percent = (int) Math.round(100.0 * completed / (double) total);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                statusLabel.setText("Scanning for music files in " + musicDir.getAbsolutePath() + " " + percent + "%");
            }
        });
    }

    private boolean isMusicFile(File file) {
        String name = file.getName().toLowerCase();
        return file.isFile() && (name.endsWith(".mp3") || name.endsWith(".flac"));
    }
}
