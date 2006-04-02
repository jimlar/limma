package limma.plugins.music;

import limma.persistence.PersistenceManager;
import limma.swing.TaskFeedback;
import limma.swing.TransactionalTask;
import limma.utils.DirectoryScanner;
import org.hibernate.Session;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

class ScanMusicFilesTask extends TransactionalTask {
    private MusicPlugin musicPlugin;
    private File musicDir;

    public ScanMusicFilesTask(MusicPlugin musicPlugin, PersistenceManager persistenceManager, MusicConfig musicConfig) {
        super(persistenceManager);
        this.musicPlugin = musicPlugin;
        musicDir = musicConfig.getMusicDir();
    }

    public void runInTransaction(TaskFeedback feedback, Session session) {
        feedback.setStatusMessage("Scanning for music files in " + musicDir.getAbsolutePath());
        List musicFiles = session.getNamedQuery("all_musicfiles").list();
        updateMusicDatabase(session, musicFiles, feedback);
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

    private void updateMusicDatabase(Session session, List musicFiles, TaskFeedback feedback) {
        int numDiskFiles = countDiskFiles();
        deleteRemovedOrOutdatedFiles(musicFiles, numDiskFiles, session, feedback);
        addMissingFiles(musicFiles, numDiskFiles, session, feedback);
    }

    private void deleteRemovedOrOutdatedFiles(List musicFiles, int numDiskFiles, Session session, TaskFeedback feedback) {
        for (ListIterator i = musicFiles.listIterator(); i.hasNext();) {
            MusicFile musicFile = (MusicFile) i.next();
            File diskFile = musicFile.getFile();
            if (!diskFile.isFile() || diskFile.lastModified() != musicFile.getLastModified().getTime()) {
                session.delete(musicFile);
                updateProgress(musicFiles.size() + numDiskFiles, i.nextIndex(), feedback);
            }
        }
    }

    private void addMissingFiles(final List musicFiles, final int numDiskFiles, final Session session, final TaskFeedback feedback) {
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
                        session.save(new MusicFile(file));
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
