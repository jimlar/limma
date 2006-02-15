package limma.plugins.video;

import junit.framework.TestCase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScanForVideosTaskTest extends TestCase {
    private ScanForVideosTask task;
    private ArrayList<File> moviesFiles;

    protected void setUp() throws Exception {
        super.setUp();
        task = new ScanForVideosTask(null, null, null, null);
        moviesFiles = new ArrayList<File>();
        moviesFiles.add(new File("/test/test-cd1.avi"));
        moviesFiles.add(new File("/test/test-cd2.avi"));
        moviesFiles.add(new File("/test/test1/cd1.avi"));
        moviesFiles.add(new File("/test/test2/cd2.avi"));
    }

    public void testFindSimilarFiles() throws Exception {
        List<File> files = task.findSimilarFiles(new File("/test/test-cd1.avi"), moviesFiles, 1);
        assertEquals(1, files.size());
        assertEquals(new File("/test/test-cd2.avi"), files.get(0));

    }

}
