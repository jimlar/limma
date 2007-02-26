package limma.persistence;

import junit.framework.TestCase;

public class MySQLDialectTest extends TestCase {
    private MySQLDialect dialect;


    protected void setUp() throws Exception {
        super.setUp();
        dialect = new MySQLDialect();
    }

    public void testGenerateInsertSQLForSimpleObject() throws Exception {
        String sql = dialect.generateInsertSQL(new Product());
        assertEquals("insert into Product (name) values (?)", sql);
    }

    public void testGenerateSelectAllSQLForSimpleObject() throws Exception {
        String sql = dialect.generateSelectAllSQL(Product.class);
        assertEquals("select id, name from Product", sql);
    }

    public void testGenerateLastInsertSQL() throws Exception {
        assertEquals("select last_insert_id()", dialect.generateLastIdentitySQL());
    }


    private static class Product {
        private long id;
        private String name;
    }
}
