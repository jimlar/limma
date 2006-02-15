package limma.plugins.video;

import limma.UIProperties;
import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.TransactionalTask;
import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class UpdateFromImdbTask extends TransactionalTask {
    private AntialiasLabel status;
    private IMDBSevice imdbSevice;
    private VideoConfig videoConfig;
    private Video video;
    private int imdbNumber;

    public UpdateFromImdbTask(PersistenceManager persistenceManager, IMDBSevice imdbSevice, VideoConfig videoConfig, Video video, int imdbNumber, UIProperties uiProperties) {
        super(persistenceManager);
        this.imdbSevice = imdbSevice;
        this.videoConfig = videoConfig;
        this.video = video;
        this.imdbNumber = imdbNumber;
        this.status = new AntialiasLabel("Fetching information from IMDB...", uiProperties);
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


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
