package limma.application.music;

import limma.application.Command;
import limma.domain.music.MusicFile;
import org.apache.commons.io.IOUtils;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class JavaSoundPlayerThread extends Thread {
    private boolean stopping = false;

    private BlockingQueue<MusicFile> queue = new LinkedBlockingQueue<MusicFile>(1);
    private AudioInputStream audioInputStream;
    private AudioFormat inputAudioFormat;
    private SourceDataLine line;
    private MusicFile currentFile;
    private JavaSoundMusicPlayer player;

    public JavaSoundPlayerThread(JavaSoundMusicPlayer player) {
        this.player = player;
        setDaemon(true);
        start();
    }

    public void run() {
        while (true) {
            currentFile = null;
            try {
                currentFile = (MusicFile) queue.take();
                stopping = false;
                openAudioInput(currentFile);

                openAudioOutputLine();

                byte[] buffer = new byte[4 * 1024];
                int bytesRead;
                while (!stopping && (bytesRead = audioInputStream.read(buffer, 0, 1024)) != -1) {
                    line.write(buffer, 0, bytesRead);
                }

                if (!stopping) {
                    line.drain();
                    System.out.println("Completed " + currentFile);
                    player.consume(Command.NEXT);
                } else {
                    System.out.println("Stopped");
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
                currentFile = null;
            }
        }
    }

    private void openAudioOutputLine() throws LineUnavailableException {
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, inputAudioFormat, AudioSystem.NOT_SPECIFIED);
        line = (SourceDataLine) AudioSystem.getLine(info);
        line.open(inputAudioFormat, AudioSystem.NOT_SPECIFIED);
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
/*

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
*/
    }

    public MusicFile getCurrentFile() {
        return currentFile;
    }

    public void queue(MusicFile musicFile) {
        this.queue.add(musicFile);
    }

    public void signalStopping() {
        stopping = true;
    }
}
