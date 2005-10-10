package limma.plugins.video;

import limma.utils.ExecUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class VideoPlayer {
    private static final String[] PLAYER_COMMAND = new String[]{"/usr/bin/mplayer", "-fixed-vo", "-fs", "-quiet", "-zoom", "-spuaa", "20", "-spugauss", "0.01"};

    public void play(Video video) throws IOException {
        ExecUtils execUtils = new ExecUtils();
        String[] command = new String[PLAYER_COMMAND.length + video.getFiles().size()];
        System.arraycopy(PLAYER_COMMAND, 0, command, 0, PLAYER_COMMAND.length);

        ArrayList sortedFiles = new ArrayList(video.getFiles());
        Collections.sort(sortedFiles, new Comparator() {
            public int compare(Object o1, Object o2) {
                VideoFile file1 = (VideoFile) o1;
                VideoFile file2 = (VideoFile) o2;
                return file1.getPath().compareToIgnoreCase(file2.getPath());
            }
        });
        Iterator filesIterator = sortedFiles.iterator();
        for (int i = PLAYER_COMMAND.length; i < command.length; i++) {
            command[i] = filesIterator.next().toString();
        }
        execUtils.executeAndWait(command);
    }
}
