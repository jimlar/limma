package limma.plugins.video;

import limma.utils.ExecUtils;
import limma.Configuration;

import java.io.IOException;
import java.io.File;
import java.util.*;

public class VideoPlayer {
    private Configuration configuration;

    public VideoPlayer(Configuration configuration) {
        this.configuration = configuration;
    }

    public void play(Video video) throws IOException {
        if (video.isDvd()) {
            play(video, "video.command.dvd");

        } else {
            play(video, "video.command.default");
        }
    }

    private void play(Video video, String commandProperty) throws IOException {
        ArrayList sortedFiles = new ArrayList(video.getFiles());
        Collections.sort(sortedFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                VideoFile file1 = (VideoFile) o1;
                VideoFile file2 = (VideoFile) o2;
                return file1.getPath().compareToIgnoreCase(file2.getPath());
            }
        });

        String[] filenames = new String[video.getFiles().size()];
        for (ListIterator i = sortedFiles.listIterator(); i.hasNext();) {
            VideoFile file = (VideoFile) i.next();
            filenames[i.previousIndex()] = file.getPath();
        }

        ExecUtils execUtils = new ExecUtils();
        execUtils.executeAndWait(configuration.getCommandLine(commandProperty, filenames));
    }
}
