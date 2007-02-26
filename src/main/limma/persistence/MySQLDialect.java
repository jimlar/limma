package limma.persistence;

public class MySQLDialect extends AbstractSQLDialect {

    public String generateLastIdentitySQL() {
        return "select last_insert_id()";
    }
}
