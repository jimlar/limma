package limma.plugins.music.player;

import limma.plugins.music.CurrentTrackPanel;
import limma.plugins.music.MusicConfig;
import limma.plugins.music.MusicFile;
import limma.utils.ExternalCommand;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExternalMusicPlayer implements MusicPlayer {
    private MusicConfig musicConfig;
    private CurrentTrackPanel currentTrackPanel;
    private PlayerThread playerThread;

    public ExternalMusicPlayer(MusicConfig musicConfig) {
        this.musicConfig = musicConfig;
        currentTrackPanel = new CurrentTrackPanel();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (playerThread != null) {
                    playerThread.kill();
                }
            }
        });
    }

    public void play(List<MusicFile> musicFiles) {
        final MusicFile musicFile = musicFiles.get(0);
        stop();

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setCurrentTrack(musicFile);
            }
        });

        playerThread = new PlayerThread(musicConfig, musicFile);
        playerThread.start();
    }

    public void stop() {
        mplayerCommand("quit");
    }

    public JComponent getPlayerPane() {
        return currentTrackPanel;
    }

    public void next() {
    }

    public void previous() {
    }

    public void ff() {
        mplayerCommand("seek 10 0");
    }

    public void rew() {
        mplayerCommand("seek -10 0");
    }

    public void pause() {
        mplayerCommand("pause");
    }

    private void mplayerCommand(String command) {
        if (playerThread != null) {
            try {
                playerThread.input(command);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class PlayerThread extends Thread {
        private MusicConfig musicConfig;
        private MusicFile musicFile;
        private Process process;

        public PlayerThread(MusicConfig musicConfig, MusicFile musicFile) {
            this.musicConfig = musicConfig;
            this.musicFile = musicFile;
        }

        public void run() {
            try {
                ExternalCommand playerCommand = musicConfig.getMPlayerCommand();
                process = Runtime.getRuntime().exec(playerCommand.getCommandLine(new String[]{"-slave", musicFile.getFile().getAbsolutePath()}));

                input("get_time_length");

                String line;
                int playedSeconds = 0;
                setPlayedSeconds(playedSeconds);
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("ANS_LENGTH=")) {
                        setTrackLengthSeconds(Integer.parseInt(line.substring("ANS_LENGTH=".length())));
                    }
                    Pattern p = Pattern.compile(".: *(\\d*):?(\\d*).(\\d).*");
                    Matcher m = p.matcher(line);
                    if (m.matches()) {
                        int newPlayedSeconds;
                        if (m.group(2).equals("")) {
                            newPlayedSeconds = Integer.parseInt(m.group(1));
                        } else {
                            newPlayedSeconds = Integer.parseInt(m.group(1)) * 60 + Integer.parseInt(m.group(2));
                        }
                        if (playedSeconds != newPlayedSeconds) {
                            playedSeconds = newPlayedSeconds;
                            setPlayedSeconds(playedSeconds);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally{
                kill();
            }
        }

        private void setPlayedSeconds(int seconds) {
            System.out.println("seconds = " + seconds);
        }

        private void setTrackLengthSeconds(int seconds) {
            System.out.println("trackLength = " + seconds);
        }

        public void input(String command) throws IOException {
            if (process != null) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
                bw.write(command);
                bw.newLine();
                bw.flush();
            }
        }

        public void kill() {
            if (process != null) {
                process.destroy();
                process = null;
            }
        }
    }
}
