package limma.plugins.music.player;

import limma.plugins.music.*;
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

    private List<MusicFile> playList;

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

    private void startPlaying(final MusicFile musicFile) {
        stop();
        if (musicFile != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    currentTrackPanel.setCurrentTrack(musicFile);
                }
            });

            playerThread = new PlayerThread(musicConfig, musicFile, this);
            playerThread.start();
        }
    }

    public void play(TrackContainerNode trackContainerNode) {
        setPlayList(trackContainerNode.getAllMusicFiles());
        startPlaying(getNextFile());
    }

    public void play(TrackNode trackNode) {
        setPlayList(trackNode.getTrackContainer().getAllMusicFiles());
        startPlaying(trackNode.getMusicFile());
    }

    public void stop() {
        mplayerCommand("quit");
    }

    public JComponent getPlayerPane() {
        return currentTrackPanel;
    }

    public void next() {
        startPlaying(getNextFile());
    }

    public void previous() {
        startPlaying(getPreviousFile());
    }

    private MusicFile getNextFile() {
        int index = getIndexOfCurrentFile();
        if (index == -1 && !playList.isEmpty()) {
            return playList.get(0);
        }
        if (index < playList.size() - 1) {
            return playList.get(index + 1);
        }
        return null;
    }

    private int getIndexOfCurrentFile() {
        int index = -1;
        if (playerThread != null) {
            index = playList.indexOf(playerThread.getMusicFile());
        }
        return index;
    }

    private MusicFile getPreviousFile() {
        return null;
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

    private void setPlayedSeconds(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setPlayedSeconds(seconds);
            }
        });
    }

    private void setTrackLengthSeconds(final int seconds) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                currentTrackPanel.setTrackLengthSeconds(seconds);
            }
        });
    }

    private synchronized void filePlayed(MusicFile musicFile) {
    }

    public void setPlayList(List<MusicFile> playList) {
        this.playList = playList;
    }

    private static class PlayerThread extends Thread {
        private MusicConfig musicConfig;
        private MusicFile musicFile;
        private ExternalMusicPlayer player;
        private Process process;

        public PlayerThread(MusicConfig musicConfig, MusicFile musicFile, ExternalMusicPlayer player) {
            this.musicConfig = musicConfig;
            this.musicFile = musicFile;
            this.player = player;
        }

        public void run() {
            try {
                ExternalCommand playerCommand = musicConfig.getMPlayerCommand();
                process = Runtime.getRuntime().exec(playerCommand.getCommandLine(new String[]{"-slave", musicFile.getFile().getAbsolutePath()}));

                input("get_time_length");

                String line;
                int playedSeconds = 0;
                player.setPlayedSeconds(playedSeconds);
                BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                while ((line = br.readLine()) != null) {
                    if (line.startsWith("ANS_LENGTH=")) {
                        player.setTrackLengthSeconds(Integer.parseInt(line.substring("ANS_LENGTH=".length())));
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
                            player.setPlayedSeconds(playedSeconds);
                        }
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                kill();
                player.filePlayed(musicFile);
            }
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

        public MusicFile getMusicFile() {
            return musicFile;
        }
    }
}
