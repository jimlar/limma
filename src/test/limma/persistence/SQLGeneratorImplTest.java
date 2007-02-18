package limma.persistence;

import junit.framework.TestCase;

public class SQLGeneratorImplTest extends TestCase {
    private SQLGeneratorImpl sqlGenerator;


    protected void setUp() throws Exception {
        super.setUp();
        sqlGenerator = new SQLGeneratorImpl();
    }

    public void testGenerateInsertSQLForSimpleObject() throws Exception {
        String sql = sqlGenerator.generateInsertSQL(new Product());
        assertEquals("insert into Product (name) values (?)", sql);
    }

    public void testGenerateSelectAllSQLForSimpleObject() throws Exception {
        String sql = sqlGenerator.generateSelectAllSQL(Product.class);
        assertEquals("select id, name from Product", sql);
    }

    public void testGenerateLastInsertSQLForHSQL() throws Exception {
        assertEquals("call identity()", sqlGenerator.generateLastIdentitySQL());
    }

    public void testGenerateLastInsertSQLForMySQL() throws Exception {
        assertEquals("select last_insert_id()", sqlGenerator.generateLastIdentitySQL());
    }


    private static class Product {
        private long id;
        private String name;
    }
}
