package limma.persistence;

import limma.AbstractConfiguration;

public class PersistenceConfigImpl extends AbstractConfiguration implements PersistenceConfig {

    public PersistenceConfigImpl() {
        super("mysql");
    }

    public String getHost() {
        return getString("host");
    }

    public String getDatabase() {
        return getString("database");
    }

    public String getUsername() {
        return getString("username");
    }

    public String getPassword() {
        return getString("password");
    }
}
