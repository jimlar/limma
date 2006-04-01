package limma.plugins.video;

import limma.swing.Task;
import limma.swing.TaskFeedback;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

class DetectImdbNumberTask implements Task {
    private static final String[] IMDB_PREFIXES = {"imdb.com/title/tt",
                                                   "imdb.com/Title?"};
    private static final long MAX_FILE_SIZE_TO_SEARCH = 100 * 1024;
    private Video video;
    private JTextField imdbNumberTextField;

    public DetectImdbNumberTask(Video video, JTextField imdbNumberTextField) {
        this.video = video;
        this.imdbNumberTextField = imdbNumberTextField;
    }

    public void run(TaskFeedback feedback) {
        feedback.setStatusMessage("Detecting IMDB number...");
        Set<File> directories = new HashSet<File>();

        for (Iterator i = video.getFiles().iterator(); i.hasNext();) {
            VideoFile file = (VideoFile) i.next();
            directories.add(file.getFile().getParentFile());
        }

        for (Iterator<File> i = directories.iterator(); i.hasNext();) {
            File dir = i.next();
            File[] files = dir.listFiles();
            for (int j = 0; files != null && j < files.length; j++) {
                File file = files[j];
                if (file.length() <= MAX_FILE_SIZE_TO_SEARCH) {
                    int imdbNumber = detectImdbNumber(file);
                    if (imdbNumber != 0) {
                        imdbNumberTextField.setText(String.valueOf(imdbNumber));
                        return;
                    }
                }
            }
        }
    }

    private int detectImdbNumber(File file) {
        System.out.println("Searching for IMDB number in file: " + file.getAbsolutePath());
        try {
            String fileData = FileUtils.readFileToString(file, "iso-8859-1");

            for (int i = 0; i < IMDB_PREFIXES.length; i++) {
                String prefix = IMDB_PREFIXES[i];

                int prefixIndex = fileData.indexOf(prefix);
                if (prefixIndex != -1) {
                    int numberIndex = prefixIndex + prefix.length();
                    String number = fileData.substring(numberIndex, numberIndex + 7);
                    try {
                        return Integer.parseInt(number);
                    } catch (NumberFormatException e) {
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
