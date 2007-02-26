package limma.persistence;

public class HSQLDialect extends AbstractSQLDialect {

    public String generateLastIdentitySQL() {
        return "call identity()";
    }
}
