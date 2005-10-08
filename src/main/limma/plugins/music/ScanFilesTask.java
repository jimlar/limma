package limma.plugins.music;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.Task;
import limma.utils.DirectoryScanner;

import javax.swing.*;
import java.io.File;

class ScanFilesTask implements Task {
    private MusicPlugin musicPlugin;
    private PersistenceManager persistenceManager;
    private File musicDir;
    private AntialiasLabel statusLabel;
    private int numFiles;
    private int filesScanned;

    public ScanFilesTask(MusicPlugin musicPlugin, PersistenceManager persistenceManager) {
        this.musicPlugin = musicPlugin;
        this.persistenceManager = persistenceManager;
        musicDir = new File("/media/music/");
        statusLabel = new AntialiasLabel("Scanning for music files in " + musicDir.getAbsolutePath());
    }

    public JComponent createComponent() {
        return statusLabel;
    }

    public void run() {
        persistenceManager.deleteAll(MusicFile.class);
        numFiles = countMusicFiles();
        scanForMusicFiles();
        musicPlugin.reloadFileList();
    }

    private int countMusicFiles() {
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

    private void scanForMusicFiles() {
        DirectoryScanner scanner = new DirectoryScanner(musicDir, true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isMusicFile(file)) {
                    persistenceManager.create(new MusicFile(file));
                    filesScanned++;
                    updateProgress();
                }
                return true;
            }
        });
    }

    private void updateProgress() {
        if (numFiles == 0) {
            return;
        }
        final int percent = (int) Math.round(100.0 * filesScanned / (double) numFiles);
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
