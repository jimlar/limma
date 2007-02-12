package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.TaskFeedback;
import limma.swing.TransactionalTask;
import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class UpdateFromImdbTask extends TransactionalTask {
    private IMDBService imdbService;
    private VideoConfig videoConfig;
    private Video video;
    private int imdbNumber;

    public UpdateFromImdbTask(PersistenceManager persistenceManager, IMDBService imdbService, VideoConfig videoConfig, Video video, int imdbNumber) {
        super(persistenceManager);
        this.imdbService = imdbService;
        this.videoConfig = videoConfig;
        this.video = video;
        this.imdbNumber = imdbNumber;
    }

    public void runInTransaction(TaskFeedback feedback, PersistenceManager persistenceManager) {
        feedback.setStatusMessage("Fetching information from IMDB...");
        try {
            final IMDBInfo info = imdbService.getInfo(imdbNumber);
            video.setImdbNumber(info.getImdbNumber());
            video.setTitle(info.getTitle());
            video.setDirector(info.getDirector());
            video.setRuntime(info.getRuntime());
            video.setPlot(info.getPlot());
            video.setRating(info.getRating());
            video.setYear(info.getYear());
            persistenceManager.save(video);

            donwloadCoverIfNeeded(info, feedback);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void donwloadCoverIfNeeded(IMDBInfo info, TaskFeedback feedback) throws IOException {
        if (info.getCover() == null) {
            return;
        }
        feedback.setStatusMessage("Downloading cover image...");

        File posterFile = new File(videoConfig.getPosterDir(), String.valueOf(video.getImdbNumber()));

        InputStream in = null;
        FileOutputStream out = null;
        HttpURLConnection urlConnection = null;
        try {
            posterFile.getParentFile().mkdirs();
            System.out.println("Fetching movie cover from: " + info.getCover());
            urlConnection = (HttpURLConnection) new URL(info.getCover()).openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDefaultUseCaches(false);
            in = urlConnection.getInputStream();
            out = new FileOutputStream(posterFile);
            CopyUtils.copy(in, out);
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(out);
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}
