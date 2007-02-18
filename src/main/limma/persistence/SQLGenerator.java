package limma.persistence;

import java.util.List;

public interface SQLGenerator {
    String generateInsertSQL(Object o);

    String generateSelectAllSQL(Class persistentClass);

    List<String> getColumns(Class persistentClass, boolean includeIdColumn);

    String generateLastIdentitySQL();
}
