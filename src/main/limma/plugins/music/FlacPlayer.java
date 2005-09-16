package limma.plugins.music;

import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FlacPlayer implements SoundPlayer {
    private PlayerThread thread;

    public void play(File file) {
        if (thread != null) {
            thread.shutdown();
        }

        thread = new PlayerThread(file);
        thread.start();
    }

    private static class PlayerThread extends Thread {
        private File soundFile;
        private Processor processor;

        public PlayerThread(File soundFile) {
            this.soundFile = soundFile;
        }

        public void shutdown() {
            processor.abort();
            interrupt();
        }

        public void run() {
            processor = new Processor();
            try {
                FileInputStream is = new FileInputStream(soundFile);
                FLACDecoder decoder = new FLACDecoder(is);

                decoder.addPCMProcessor(processor);
                decoder.decode();
            } catch (IOException eofexception) {
            } finally {
                processor.close();
            }


        }
    }

    private static class Processor implements PCMProcessor {
        private AudioFormat fmt;
        private DataLine.Info info;
        private SourceDataLine line;

        public void processStreamInfo(StreamInfo streamInfo) {
            try {
                fmt = streamInfo.getAudioFormat();
                info = new DataLine.Info(SourceDataLine.class, fmt, -1);
                line = (SourceDataLine) AudioSystem.getLine(info);
                line.open(fmt, -1);
                line.start();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }
        }

        public void processPCM(ByteData pcm) {
            line.write(pcm.getData(), 0, pcm.getLen());
        }

        public void close() {
            if (line != null) {
                line.drain();
                line.close();
            }
        }

        public void abort() {
            if (line != null) {
                line.close();
            }
        }
    }
}
