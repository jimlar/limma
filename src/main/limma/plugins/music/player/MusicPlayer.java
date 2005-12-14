package limma.plugins.music.player;

import limma.plugins.music.MusicFile;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.io.IOUtils;

public class MusicPlayer extends Thread {
    private PlayerListener listener;
    private BlockingQueue queue = new LinkedBlockingQueue(1);
    private AudioInputStream audioInputStream;
    private AudioFormat inputAudioFormat;
    private SourceDataLine line;
    private boolean stopping = false;

    public MusicPlayer(PlayerListener listener) {
        this.listener = listener;
        setDaemon(true);
        start();
    }

    public void play(final MusicFile musicFile) {
        stopPlaying();
        try {
            queue.put(musicFile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying() {
        stopping = true;
    }


    public void run() {
        while (true) {
            MusicFile musicFile = null;
            try {
                musicFile = (MusicFile) queue.take();
                stopping = false;
                openAudioInput(musicFile);

                openAudioOutputLine();

                byte[] buffer = new byte[4 * 1024];
                int bytesRead;
                while (!stopping && (bytesRead = audioInputStream.read(buffer, 0, 1024)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                if (!stopping) {
                    line.drain();
                    listener.completed(musicFile);
                    System.out.println("Completed " + musicFile);
                } else {
                    System.out.println("Stopped");
                    listener.stopped(musicFile);
                }

            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (line != null) {
                    line.close();
                }
                IOUtils.closeQuietly(audioInputStream);
            }
        }
    }

    private void openAudioOutputLine() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, inputAudioFormat, AudioSystem.NOT_SPECIFIED);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(inputAudioFormat, AudioSystem.NOT_SPECIFIED);
        System.out.println("line.getBufferSize() = " + line.getBufferSize());
        line.start();
    }

    private void openAudioInput(MusicFile musicFile) throws IOException, UnsupportedAudioFileException {
        audioInputStream = AudioSystem.getAudioInputStream(musicFile.getFile());
        inputAudioFormat = audioInputStream.getFormat();

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
    }
}
