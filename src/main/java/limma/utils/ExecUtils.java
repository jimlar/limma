package limma.utils;

import java.io.IOException;
import java.io.OutputStream;

public class ExecUtils {
    public int executeAndWait(String[] command) throws IOException {
        return executeAndWait(command, System.out, System.err);
    }

    public int executeAndWait(String[] command, OutputStream stdOut, OutputStream stdErr) throws IOException {
        int exitValue = -1;
        Process process = null;
        StreamForwarder errorForwarder = null;
        StreamForwarder outForwarder = null;

        try {
            process = Runtime.getRuntime().exec(command);
            errorForwarder = new StreamForwarder(process.getErrorStream(), stdErr);
            outForwarder = new StreamForwarder(process.getInputStream(), stdOut);

            exitValue = process.waitFor();

            outForwarder.waitFor(10000);
            errorForwarder.waitFor(10000);

        } catch (InterruptedException e) {
            throw new RuntimeException("Process interrupted", e);
        } finally {
            if (process != null) {
                process.destroy();
            }

            if (errorForwarder != null) {
                errorForwarder.stop();
            }

            if (outForwarder != null) {
                outForwarder.stop();
            }
        }
        return exitValue;
    }


}
