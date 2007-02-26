package limma.persistence;

import junit.framework.TestCase;

public class HSQLDialectTest extends TestCase {
    private HSQLDialect dialect;


    protected void setUp() throws Exception {
        super.setUp();
        dialect = new HSQLDialect();
    }

    public void testGenerateInsertSQLForSimpleObject() throws Exception {
        String sql = dialect.generateInsertSQL(new Product());
        assertEquals("insert into Product (name) values (?)", sql);
    }

    public void testGenerateSelectAllSQLForSimpleObject() throws Exception {
        String sql = dialect.generateSelectAllSQL(Product.class);
        assertEquals("select id, name from Product", sql);
    }

    public void testGenerateLastInsertSQLFor() throws Exception {
        assertEquals("call identity()", dialect.generateLastIdentitySQL());
    }

    private static class Product {
        private long id;
        private String name;
    }
}
