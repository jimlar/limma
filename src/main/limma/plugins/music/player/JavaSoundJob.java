package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

import javax.sound.sampled.*;
import java.io.IOException;

public class JavaSoundJob extends PlayerJob {
    private MusicPlayer player;
    private boolean shutdown;

    public JavaSoundJob(MusicFile musicFile, MusicPlayer musicPlayer) {
        super(musicFile);
        player = musicPlayer;
    }

    public void abort() {
        shutdown = true;
    }

    public void run() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getMusicFile().getFile());
            AudioFormat inputAudioFormat = audioInputStream.getFormat();

            boolean isSupportedDirectly = AudioSystem.isLineSupported(new DataLine.Info(SourceDataLine.class, inputAudioFormat, AudioSystem.NOT_SPECIFIED));

            if (!isSupportedDirectly) {
                AudioFormat resampleFormat = new AudioFormat(inputAudioFormat.getEncoding(),
                                                             inputAudioFormat.getSampleRate(),
                                                             inputAudioFormat.getSampleSizeInBits(),
                                                             inputAudioFormat.getChannels(),
                                                             inputAudioFormat.getChannels() * (inputAudioFormat.getSampleSizeInBits() / 8),
                                                             inputAudioFormat.getSampleRate(),
                                                             false);

                /* Wrap input in conversion filter */
                audioInputStream = AudioSystem.getAudioInputStream(resampleFormat, audioInputStream);
                inputAudioFormat = audioInputStream.getFormat();
            }

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, inputAudioFormat, AudioSystem.NOT_SPECIFIED);
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(inputAudioFormat, AudioSystem.NOT_SPECIFIED);
            line.start();

            byte[] buffer = new byte[128000];
            int bytesRead;
            while ((bytesRead = audioInputStream.read(buffer, 0, buffer.length)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();

        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if (shutdown) {
                player.signalStopped(getMusicFile());
            } else {
                player.signalCompleted(getMusicFile());
            }
        }
    }
}
