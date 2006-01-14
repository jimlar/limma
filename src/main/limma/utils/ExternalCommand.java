package limma.utils;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;

public class ExternalCommand {
    private String commandLine;

    public ExternalCommand(String commandLine) {
        this.commandLine = commandLine;
    }

    public void execute() throws IOException {
        execute(new String[0]);
    }

    public void execute(String[] appendToCommand) throws IOException {
        String[] result = getCommandLine(appendToCommand);
        new ExecUtils().executeAndWait(result);
    }

    public String[] getCommandLine(String appendToCommand) {
        return getCommandLine(new String[] { appendToCommand });
    }

    public String[] getCommandLine(String[] appendToCommand) {
        String[] command = StringUtils.split(commandLine, ' ');
        String[] result = new String[command.length + appendToCommand.length];
        System.arraycopy(command, 0, result, 0, command.length);
        System.arraycopy(appendToCommand, 0, result, command.length, appendToCommand.length);
        return result;
    }

    public void execute(String appendToCommand) throws IOException {
        execute(new String[] { appendToCommand });
    }
}
