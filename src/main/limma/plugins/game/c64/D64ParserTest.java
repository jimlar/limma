package limma.plugins.game.c64;

import java.util.List;
import java.io.FileInputStream;

import junit.framework.TestCase;

public class D64ParserTest extends TestCase {
    public void testReadLastNinja2Disk() throws Exception {
        D64Parser d64Parser = new D64Parser(new FileInputStream("testdata/last-ninja2.d64"));
        List gameNames = d64Parser.getDirectoryEntries();
        System.out.println("gameNames = " + gameNames);
    }
}
