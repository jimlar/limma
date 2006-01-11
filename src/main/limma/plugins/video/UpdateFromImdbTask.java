package limma.plugins.video;

import limma.swing.TransactionalTask;
import limma.swing.AntialiasLabel;
import limma.persistence.PersistenceManager;

import javax.swing.*;

import org.hibernate.Session;
import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class UpdateFromImdbTask extends TransactionalTask {
    private AntialiasLabel status = new AntialiasLabel("Fetching information from IMDB...");
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private Video video;
    private int imdbNumber;

    public UpdateFromImdbTask(PersistenceManager persistenceManager, IMDBSevice imdbSevice, VideoConfig videoConfig, Video video, int imdbNumber) {
        super(persistenceManager);
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.video = video;
        this.imdbNumber = imdbNumber;
    }

    public JComponent createComponent() {
        return status;
    }

    public void runInTransaction(Session session) {
        try {
            final IMDBInfo info = imdbSevice.getInfo(imdbNumber);
            video.setImdbNumber(info.getImdbNumber());
            video.setTitle(info.getTitle());
            video.setDirector(info.getDirector());
            video.setRuntime(info.getRuntime());
            video.setPlot(info.getPlot());
            video.setRating(info.getRating());
            video.setYear(info.getYear());
            session.merge(video);

            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    status.setText("Downloading cover image...");
                }
            });

            File posterFile = new File(videoConfig.getPosterDir(), String.valueOf(video.getImdbNumber()));

            InputStream in = null;
            FileOutputStream out = null;
            HttpURLConnection urlConnection = null;
            try {
                posterFile.getParentFile().mkdirs();
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


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
