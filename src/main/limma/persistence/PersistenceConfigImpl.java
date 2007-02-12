package limma.persistence;

import limma.AbstractConfiguration;

public class PersistenceConfigImpl extends AbstractConfiguration implements PersistenceConfig {

    public PersistenceConfigImpl() {
        super("mysql");
    }

    public String getUrl() {
        return getString("url");
    }

    public String getUsername() {
        return getString("username");
    }

    public String getPassword() {
        return getString("password");
    }

    public String getDriver() {
        return getString("driver");
    }
}
