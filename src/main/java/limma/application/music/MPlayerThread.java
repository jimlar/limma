package limma.application.music;

import limma.application.Command;
import limma.domain.music.MusicFile;
import limma.utils.ExternalCommand;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MPlayerThread extends Thread {
    private MusicConfig musicConfig;
    private MusicFile musicFile;
    private ExternalMusicPlayer player;
    private Process process;
    private boolean forceStopped = false;

    public MPlayerThread(MusicConfig musicConfig, MusicFile musicFile, ExternalMusicPlayer player) {
        this.musicConfig = musicConfig;
        this.musicFile = musicFile;
        this.player = player;
    }

    public void run() {
        try {
            ExternalCommand playerCommand = musicConfig.getMPlayerCommand();
            process = Runtime.getRuntime().exec(playerCommand.getCommandLine(new String[]{"-slave", musicConfig.getDiskFile(musicFile).getAbsolutePath()}));

            input("get_time_length");

            String line;
            int playedSeconds = 0;
            player.setPlayedSeconds(playedSeconds);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("ANS_LENGTH=")) {
                    player.setTrackLengthSeconds((int) Double.parseDouble(line.substring("ANS_LENGTH=".length())));
                }
                try {
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
                } catch (NumberFormatException e) {
                    System.out.println("can parse mplayer line: " + line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            kill();
            if (!forceStopped) {
                player.consume(Command.NEXT);
            }
        }
    }

    public synchronized void input(String command) throws IOException {
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

    public void quit() {
        forceStopped = true;
        try {
            input("quit");
            join(1000);
            kill();
        } catch (IOException e) {
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
