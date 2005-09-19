package limma.plugins.music.player;

import limma.plugins.music.MusicFile;
import org.kc7bfi.jflac.FLACDecoder;
import org.kc7bfi.jflac.PCMProcessor;
import org.kc7bfi.jflac.metadata.StreamInfo;
import org.kc7bfi.jflac.util.ByteData;

import javax.sound.sampled.*;
import java.io.FileInputStream;
import java.io.IOException;

class FlacPlayerThread extends PlayerThread {
    private Processor processor;

    public FlacPlayerThread(MusicFile musicFile) {
        super(musicFile);
    }

    public void shutdown() {
        processor.abort();
        interrupt();
    }

    public void run() {
        processor = new Processor();
        try {
            FileInputStream is = new FileInputStream(getMusicFile().getFile());
            FLACDecoder decoder = new FLACDecoder(is);

            decoder.addPCMProcessor(processor);
            decoder.decode();
        } catch (IOException eofexception) {
        } finally {
            processor.close();
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
