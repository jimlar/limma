package limma.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


public class StreamForwarder {
    private static final short BUFFER_SIZE = 128;
    private Thread thread;
    private ReentrantLock completionLatch = new ReentrantLock();

    public StreamForwarder(final InputStream inputStream, final OutputStream outputStream) {
        thread = new Thread() {
            public void run() {
                byte[] buffer = new byte[BUFFER_SIZE];

                int readLength;
                completionLatch.lock();
                try {
                    while ((readLength = inputStream.read(buffer)) > 0 && !interrupted()) {
                        outputStream.write(buffer, 0, readLength);
                    }
                } catch (IOException e) {
                } finally {
                    completionLatch.unlock();
                }
            }
        };
        thread.start();

    }

    public void stop() {
        thread.interrupt();
    }

    /**
     * @return true if we completed before timeout
     */
    public boolean waitFor(long timeout) {
        try {
            return completionLatch.tryLock(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }
}
