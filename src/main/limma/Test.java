package limma;

import javax.sound.sampled.*;
import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("/tmp/test.mp3"));
            AudioFormat inputAudioFormat = audioInputStream.getFormat();

            /* Wrap input in PCM conversion filter */
            AudioFormat pcmConversionFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                                                              inputAudioFormat.getSampleRate(),
                                                              16,
                                                              inputAudioFormat.getChannels(),
                                                              inputAudioFormat.getChannels() * (16 / 8),
                                                              inputAudioFormat.getSampleRate(),
                                                              false);

            System.out.println("Converting from: " + inputAudioFormat);
            System.out.println("             to: " + pcmConversionFormat);

            audioInputStream = AudioSystem.getAudioInputStream(pcmConversionFormat, audioInputStream);
            inputAudioFormat = audioInputStream.getFormat();

            AudioFormat resampleFormat = new AudioFormat(pcmConversionFormat.getEncoding(),
                                                         48000,
                                                         pcmConversionFormat.getSampleSizeInBits(),
                                                         pcmConversionFormat.getChannels(),
                                                         pcmConversionFormat.getFrameSize(),
                                                         48000,
                                                         false);

            System.out.println("Converting from: " + inputAudioFormat);
            System.out.println("             to: " + resampleFormat);

            audioInputStream = AudioSystem.getAudioInputStream(resampleFormat, audioInputStream);
            inputAudioFormat = audioInputStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, inputAudioFormat, AudioSystem.NOT_SPECIFIED);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(inputAudioFormat, AudioSystem.NOT_SPECIFIED);
            line.start();

            byte[] buffer = new byte[4 * 1024 * 1024];
            int bytesRead;
            while ((bytesRead = audioInputStream.read(buffer, 0, 1024)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
