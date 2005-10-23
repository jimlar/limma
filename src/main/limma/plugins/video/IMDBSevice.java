package limma.plugins.video;

import java.io.IOException;

public interface IMDBSevice {
    IMDBInfo getInfo(int imdbNumber) throws IOException;
}
