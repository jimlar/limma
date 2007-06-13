package limma.plugins.music;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import limma.domain.music.MusicFile;
import limma.domain.music.MusicRepository;
import limma.swing.Task;
import limma.swing.TaskFeedback;
import limma.utils.DirectoryScanner;

class ScanMusicFilesTask implements Task {
    private MusicPlugin musicPlugin;
    private MusicRepository musicRepository;
    private File musicDir;

    public ScanMusicFilesTask(MusicPlugin musicPlugin, MusicConfig musicConfig, MusicRepository musicRepository) {
        this.musicPlugin = musicPlugin;
        this.musicRepository = musicRepository;
        musicDir = musicConfig.getMusicDir();
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Scanning for music files in " + musicDir.getAbsolutePath());
        List musicFiles = musicRepository.getAll();
        updateMusicDatabase(musicFiles, feedback);
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

    private void updateMusicDatabase(List musicFiles, TaskFeedback feedback) {
        int numDiskFiles = countDiskFiles();
        deleteRemovedOrOutdatedFiles(musicFiles, numDiskFiles, feedback);
        addMissingFiles(musicFiles, numDiskFiles, feedback);
    }

    private void deleteRemovedOrOutdatedFiles(List musicFiles, int numDiskFiles, TaskFeedback feedback) {
        for (ListIterator i = musicFiles.listIterator(); i.hasNext();) {
            MusicFile musicFile = (MusicFile) i.next();
            File diskFile = musicFile.getFile();
            if (!diskFile.isFile() || diskFile.lastModified() != musicFile.getLastModified().getTime()) {
                musicRepository.remove(musicFile);
                updateProgress(musicFiles.size() + numDiskFiles, i.nextIndex(), feedback);
            }
        }
    }

    private void addMissingFiles(final List musicFiles, final int numDiskFiles, final TaskFeedback feedback) {
        final int numDatabaseFiles = musicFiles.size();
        final ArrayList<File> persistentFiles = new ArrayList<File>();
        for (Iterator i = musicFiles.iterator(); i.hasNext();) {
            MusicFile musicFile = (MusicFile) i.next();
            persistentFiles.add(musicFile.getFile());
        }

        final int[] filesScanned = new int[]{0};
        DirectoryScanner scanner = new DirectoryScanner(musicDir, true);
        scanner.accept(new DirectoryScanner.Visitor() {
            public boolean visit(File file) {
                if (isMusicFile(file)) {
                    if (!persistentFiles.contains(file)) {
                        musicRepository.add(new MusicFile(file));
                    }
                    filesScanned[0]++;
                    updateProgress(numDatabaseFiles + numDiskFiles, filesScanned[0], feedback);
                }
                return true;
            }
        });
    }

    private void updateProgress(int total, int completed, TaskFeedback feedback) {
        if (total == 0) {
            return;
        }
        final int percent = (int) Math.round(100.0 * completed / (double) total);
        feedback.setStatusMessage("Scanning for music files in " + musicDir.getAbsolutePath() + " " + percent + "%");
    }

    private boolean isMusicFile(File file) {
        String name = file.getName().toLowerCase();
        return file.isFile() && (name.endsWith(".mp3") || name.endsWith(".flac") || name.endsWith(".ogg"));
    }
}
