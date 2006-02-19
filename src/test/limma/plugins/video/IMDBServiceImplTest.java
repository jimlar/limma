package limma.plugins.video;

import com.gargoylesoftware.htmlunit.*;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class IMDBServiceImplTest extends TestCase {

    public void testGetInfoFor2001ASpaceOdyssey() throws Exception {
        IMDBServiceImpl imdbSevice = createImdbServiceWithMockWebClient("imdb-2001.html");

        IMDBInfo info = imdbSevice.getInfo(62622);
        assertEquals(62622, info.getImdbNumber());
        assertEquals("2001: A Space Odyssey", info.getTitle());
        assertEquals(1968, info.getYear());
        assertEquals("Stanley Kubrick", info.getDirector());
        assertEquals("Mankind finds a mysterious, obviously artificial, artifact buried on the moon and, with the intelligent computer HAL, sets off on a quest.", info.getPlot());
        assertEquals("USA:160 min (premiere cut) / USA:141 min", info.getRuntime());
        assertEquals("8.3/10", info.getRating());
        assertEquals("http://ia.imdb.com/media/imdb/01/I/29/26/31m.jpg", info.getCover());
    }

    public void testGetInfoForMovieWithBarelyAnyInfo() throws Exception {
        IMDBServiceImpl imdbSevice = createImdbServiceWithMockWebClient("imdb-little-info.html");

        IMDBInfo info = imdbSevice.getInfo(497874);
        assertEquals(497874, info.getImdbNumber());
        assertEquals("27 sekundmeter snö", info.getTitle());
        assertEquals(2005, info.getYear());
        assertNull(info.getDirector());
        assertNull(info.getPlot());
        assertEquals("Sweden:180 min  / Sweden:180 min (2 parts)", info.getRuntime());
        assertEquals("5.4/10", info.getRating());
        assertNull(info.getCover());
    }

    private IMDBServiceImpl createImdbServiceWithMockWebClient(final String mockPage) {
        IMDBServiceImpl imdbSevice = new IMDBServiceImpl(new WebClient() {
            public Page getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
                return getPage(getCurrentWindow(), new WebRequestSettings(new File("testdata", mockPage).toURL()));
            }
        });
        return imdbSevice;
    }
}
