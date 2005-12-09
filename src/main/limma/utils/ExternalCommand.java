package limma.utils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class ExternalCommand {
    private String commandLine;

    public ExternalCommand(String commandLine) {
        this.commandLine = commandLine;
    }

    public void execute(String[] appendToCommand) throws IOException {
        ExecUtils execUtils = new ExecUtils();

        String[] command = StringUtils.split(commandLine, ' ');
        String[] result = new String[command.length + appendToCommand.length];
        System.arraycopy(command, 0, result, 0, command.length);
        System.arraycopy(appendToCommand, 0, result, command.length, appendToCommand.length);
        execUtils.executeAndWait(result);
    }

    public void execute(String appendToCommand) throws IOException {
        execute(new String[] { appendToCommand });
    }
}
