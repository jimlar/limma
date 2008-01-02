package limma.ui.video;

import limma.application.video.IMDBInfo;
import limma.application.video.IMDBService;
import limma.application.video.VideoConfig;
import limma.domain.video.Video;
import limma.domain.video.VideoRepository;
import limma.ui.dialogs.Task;
import limma.ui.dialogs.TaskFeedback;
import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class UpdateFromImdbTask implements Task {
    private IMDBService imdbService;
    private VideoConfig videoConfig;
    private Video video;
    private int imdbNumber;
    private VideoRepository videoRepository;

    public UpdateFromImdbTask(IMDBService imdbService, VideoConfig videoConfig, Video video, int imdbNumber, VideoRepository videoRepository) {
        this.imdbService = imdbService;
        this.videoConfig = videoConfig;
        this.video = video;
        this.imdbNumber = imdbNumber;
        this.videoRepository = videoRepository;
    }

    public void run(TaskFeedback feedback) {
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

            video.setGenres(info.getGenres());
            video.setActors(info.getActors());

            videoRepository.save(video);

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
