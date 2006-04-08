package limma;

import limma.utils.ExternalCommand;

public class GeneralConfigImpl extends AbstractConfiguration implements GeneralConfig {

    public GeneralConfigImpl() {
        super(null);
    }

    public ExternalCommand getShutdownCommand() {
        return new ExternalCommand(getString("shutdown.command"));
    }
}
