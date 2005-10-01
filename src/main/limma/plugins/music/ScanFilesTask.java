package limma.plugins.music;

import limma.swing.AntialiasLabel;
import limma.swing.TaskDialog;
import limma.swing.Task;
import limma.utils.DirectoryScanner;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

class ScanFilesTask implements Task {
    private MusicPlugin musicPlugin;
    private File musicDir;
    private AntialiasLabel statusLabel;
    private int numFiles;
    private int filesScanned;

    public ScanFilesTask(MusicPlugin musicPlugin) {
        this.musicPlugin = musicPlugin;
        musicDir = new File("/media/music/");
        statusLabel = new AntialiasLabel("Scanning for music files in " + musicDir.getAbsolutePath());
    }

    public JComponent createComponent() {
        return statusLabel;
    }

    public void run() {
        numFiles = countMusicFiles();
        List files = scanForMusicFiles();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(MusicPlugin.MUSIC_CACHE));
            out.writeObject(files);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
        musicPlugin.reloadFileList();
    }

    private int countMusicFiles() {
        final int[] result = new int[]{0};
        DirectoryScanner scanner = new DirectoryScanner(musicDir, true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public void visit(File file) {
                if (isMusicFile(file)) {
                    result[0]++;
                }
            }
        });
        return result[0];
    }

    private List scanForMusicFiles() {
        final ArrayList result = new ArrayList();
        DirectoryScanner scanner = new DirectoryScanner(musicDir, true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public void visit(File file) {
                if (isMusicFile(file)) {
                    result.add(new MusicFile(file));
                    filesScanned++;
                    updateProgress();
                }
            }
        });
        return result;
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
        return name.endsWith(".mp3") || name.endsWith(".flac");
    }
}
