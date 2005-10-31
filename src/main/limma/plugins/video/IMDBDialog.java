package limma.plugins.video;

import limma.persistence.PersistenceManager;
import limma.swing.AntialiasLabel;
import limma.swing.DialogManager;
import limma.swing.LimmaDialog;
import limma.swing.TransactionalTask;
import org.hibernate.Session;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.CopyUtils;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.net.HttpURLConnection;

public class IMDBDialog extends LimmaDialog {
    private DialogManager dialogManager;
    private JTextField textField;
    private Video video;
    private IMDBSevice imdbSevice;
    private PersistenceManager persistenceManager;

    public IMDBDialog(DialogManager dialogManager, Video video, IMDBSevice imdbSevice, PersistenceManager persistenceManager) {
        super(dialogManager);
        this.dialogManager = dialogManager;
        this.video = video;
        this.imdbSevice = imdbSevice;
        this.persistenceManager = persistenceManager;
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.add(new AntialiasLabel("IMDB Number:"));
        textField = new JTextField();
        textField.setOpaque(true);
        textField.setColumns(10);
        textField.setFont(AntialiasLabel.DEFAULT_FONT);
        panel.add(textField);
        add(panel);
    }

    public void open() {
        super.open();
        textField.setText(video.getImdbNumber() != 0 ? String.valueOf(video.getImdbNumber()) : "");
        textField.requestFocusInWindow();
    }

    public boolean keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                close();
                return true;
            case KeyEvent.VK_ENTER:
                updateFromImdb();
                close();
                return true;
        }
        return false;
    }

    private void updateFromImdb() {
        dialogManager.executeInDialog(new UpdateTask(persistenceManager, imdbSevice, video, getImdbNumber()));
    }

    private int getImdbNumber() {
        return Integer.parseInt(textField.getText());
    }

    private static class UpdateTask extends TransactionalTask {
        private AntialiasLabel status = new AntialiasLabel("Fetching information from IMDB...");
        private IMDBSevice imdbSevice;
        private Video video;
        private int imdbNumber;

        public UpdateTask(PersistenceManager persistenceManager, IMDBSevice imdbSevice, Video video, int imdbNumber) {
            super(persistenceManager);
            this.imdbSevice = imdbSevice;
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

                File posterFile = video.getPosterFile();

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
}
