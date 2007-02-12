package limma.persistence;

import junit.framework.TestCase;

public class JDBCPersistenceManagerTest extends TestCase {

    public void testloadAllObjects() throws Exception {
        JDBCPersistenceManager manager = new JDBCPersistenceManager(new HSQLConfig());
        manager.addPersistentClass(Order.class);
        manager.loadAll(Order.class);
    }


    public static class Order {
        private long id;
        private String name;
    }



    private static class HSQLConfig implements PersistenceConfig {
        public String getDriver() {
            return null;
        }

        public String getUrl() {
            return null;
        }

        public String getUsername() {
            return null;
        }

        public String getPassword() {
            return null;
        }
    }
}
