package limma.plugins.video;

import limma.utils.ExecUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class VideoPlayer {

    public void play(Video video) throws IOException {
        if (video.isDvd()) {
            play(video, new String[]{"/usr/bin/ogle"});

        } else {
            play(video, new String[]{"/usr/bin/mplayer", "-fixed-vo", "-fs", "-quiet", "-zoom", "-spuaa", "20", "-spugauss", "0.01"});
        }
    }

    private void play(Video video, String[] playerCommand) throws IOException {
        String[] command = new String[playerCommand.length + video.getFiles().size()];
        System.arraycopy(playerCommand, 0, command, 0, playerCommand.length);

        ArrayList sortedFiles = new ArrayList(video.getFiles());
        Collections.sort(sortedFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                VideoFile file1 = (VideoFile) o1;
                VideoFile file2 = (VideoFile) o2;
                return file1.getPath().compareToIgnoreCase(file2.getPath());
            }
        });
        Iterator filesIterator = sortedFiles.iterator();
        for (int i = playerCommand.length; i < command.length; i++) {
            command[i] = filesIterator.next().toString();
        }
        ExecUtils execUtils = new ExecUtils();
        execUtils.executeAndWait(command);
    }
}
