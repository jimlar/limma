package limma.application.video;

import java.io.IOException;

public interface IMDBService {
    IMDBInfo getInfo(int imdbNumber) throws IOException;
}
