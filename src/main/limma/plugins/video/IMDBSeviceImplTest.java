package limma.plugins.video;

import junit.framework.TestCase;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class IMDBSeviceImplTest extends TestCase {

    public void testGetInfoFor2001ASpaceOdyssey() throws Exception {
        IMDBSeviceImpl imdbSevice = new IMDBSeviceImpl();
        IMDBInfo info = imdbSevice.getInfo(62622);
        assertEquals(62622, info.getImdbNumber());
        assertEquals("2001: A Space Odyssey", info.getTitle());
        assertEquals(1968, info.getYear());
        assertEquals("Stanley Kubrick", info.getDirector());
        assertEquals("Mankind finds a mysterious, obviously artificial, artifact buried on the moon and, with the intelligent computer HAL, sets off on a quest.", info.getPlot());
        assertEquals("139 min", info.getRuntime());
        assertEquals("8.3", info.getRating());
    }

    public void test() throws Exception {
        Pattern titleAndYearPattern = Pattern.compile(".*<title>(.+) \\((\\d+).*\\)</title>.*", Pattern.MULTILINE);
        Matcher matcher = titleAndYearPattern.matcher("zczxcxz<title>2001: A Space Odyssey (1968/II)</title>zxczxcxzxzczxc");
        assertTrue(matcher.matches());
    }
}
