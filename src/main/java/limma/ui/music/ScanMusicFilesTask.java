package limma.ui.music;

import limma.domain.music.MusicRepository;
import limma.domain.music.ProgressListener;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;

class ScanMusicFilesTask implements Task {
    private MusicPlugin musicPlugin;
    private MusicRepository musicRepository;

    public ScanMusicFilesTask(MusicPlugin musicPlugin, MusicRepository musicRepository) {
        this.musicPlugin = musicPlugin;
        this.musicRepository = musicRepository;
    }

    public void run(final TaskFeedback feedback) {
        feedback.setStatusMessage("Scanning for music files...");

        ProgressListener progressListener = new ProgressListener() {
            public void progressUpdated(int total, int completed) {
                if (total == 0) {
                    return;
                }
                final int percent = (int) Math.round(100.0 * completed / (double) total);
                feedback.setStatusMessage("Reading music information (" + percent + "%)");
            }
        };
        musicRepository.scanForMusic(progressListener);
        musicPlugin.reloadFileList();
    }
}
