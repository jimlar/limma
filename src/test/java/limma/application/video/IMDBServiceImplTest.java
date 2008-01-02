package limma.application.video;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
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
        assertEquals("141 min  / 160 min (premiere cut)", info.getRuntime());
        assertEquals("8.4/10", info.getRating());
        assertEquals("http://ia.imdb.com/media/imdb/01/I/14/06/83/10m.jpg", info.getCover());

        assertEquals(2, info.getGenres().size());
        assertTrue(info.getGenres().contains("Adventure"));
        assertTrue(info.getGenres().contains("Sci-Fi"));

        assertEquals(15, info.getActors().size());
        assertEquals("Keir Dullea", info.getActors().get(0));
        assertEquals("Gary Lockwood", info.getActors().get(1));
        assertEquals("William Sylvester", info.getActors().get(2));
        assertEquals("Daniel Richter", info.getActors().get(3));
        assertEquals("Leonard Rossiter", info.getActors().get(4));
        assertEquals("Margaret Tyzack", info.getActors().get(5));
        assertEquals("Robert Beatty", info.getActors().get(6));
        assertEquals("Sean Sullivan", info.getActors().get(7));
        assertEquals("Douglas Rain", info.getActors().get(8));
        assertEquals("Frank Miller", info.getActors().get(9));
        assertEquals("Bill Weston", info.getActors().get(10));
        assertEquals("Ed Bishop", info.getActors().get(11));
        assertEquals("Glenn Beck", info.getActors().get(12));
        assertEquals("Alan Gifford", info.getActors().get(13));
        assertEquals("Ann Gillis", info.getActors().get(14));
    }

    public void testGetInfoForMovieWithBarelyAnyInfo() throws Exception {
        IMDBServiceImpl imdbSevice = createImdbServiceWithMockWebClient("imdb-little-info.html");

        IMDBInfo info = imdbSevice.getInfo(497874);
        assertEquals(497874, info.getImdbNumber());
        assertEquals("27 sekundmeter snö", info.getTitle());
        assertEquals(2005, info.getYear());
        assertEquals("Tobias Falk", info.getDirector());
        assertEquals("In 1939 ten people on an outing are caught in a mountain cabin during a snow storm together with the cabin's hostess...", info.getPlot());
        assertEquals("Sweden:180 min (2 parts) / Sweden:180 min", info.getRuntime());
        assertEquals("4.3/10", info.getRating());
        assertEquals("http://ia.imdb.com/media/imdb/01/I/26/04/20/10m.jpg", info.getCover());

        assertEquals(4, info.getGenres().size());
        assertTrue(info.getGenres().contains("Crime"));
        assertTrue(info.getGenres().contains("Drama"));
        assertTrue(info.getGenres().contains("Mystery"));
        assertTrue(info.getGenres().contains("Thriller"));

        assertEquals(11, info.getActors().size());
        assertEquals("Jacob Ericksson", info.getActors().get(0));
        assertEquals("Anders Ahlbom", info.getActors().get(1));
        assertEquals("Mylaine Hedreul", info.getActors().get(2));
        assertEquals("Ulricha Johnson", info.getActors().get(3));
        assertEquals("Niklas Falk", info.getActors().get(4));
        assertEquals("Elisabeth Carlsson", info.getActors().get(5));
        assertEquals("Malena Engström", info.getActors().get(6));
        assertEquals("Jamil Drissi", info.getActors().get(7));
        assertEquals("Jan Mybrand", info.getActors().get(8));
        assertEquals("Gunilla Abrahamsson", info.getActors().get(9));
        assertEquals("Livia Millhagen", info.getActors().get(10));

    }

    private IMDBServiceImpl createImdbServiceWithMockWebClient(final String mockPage) {
        return new IMDBServiceImpl(new WebClient() {
            public Page getPage(final URL url) throws IOException, FailingHttpStatusCodeException {
                return getPage(getCurrentWindow(), new WebRequestSettings(new File("testdata", mockPage).toURL()));
            }
        });
    }
}
