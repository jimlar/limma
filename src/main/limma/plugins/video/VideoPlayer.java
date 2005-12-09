package limma.plugins.video;

import limma.utils.ExternalCommand;

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.ListIterator;

public class VideoPlayer {
    private VideoConfig videoConfig;

    public VideoPlayer(VideoConfig videoConfig) {
        this.videoConfig = videoConfig;
    }

    public void play(Video video) throws IOException {
        play(video, getPlayer(video));
    }

    private ExternalCommand getPlayer(Video video) {
        if (isDvd(video)) {
            return videoConfig.getDvdPlayerCommand();

        } else {
            return videoConfig.getDefaultPlayerCommand();
        }
    }

    private boolean isDvd(Video video) {
        if (video.getFiles().size() != 1) {
            return false;
        }

        VideoFile file = (VideoFile) video.getFiles().iterator().next();

        /* Its a dvd on disk directory */
        if (new File(file.getFile(), "VIDEO_TS.IFO").isFile()) {
            return true;
        }

        /* 4GB files are considered to be dvds */
        if (file.getFile().isFile() && file.getFile().length() > (4L * 1024 * 1024 * 1024)) {
            return true;
        }

        return false;
    }

    private void play(Video video, ExternalCommand playerCommand) throws IOException {
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

        playerCommand.execute(filenames);
    }
}
