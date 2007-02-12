package limma.persistence;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SQLGeneratorImpl implements SQLGenerator {

    public String generateInsertSQL(Object o) {
        Class persistentClass = o.getClass();
        List<String> columns = getColumns(persistentClass, false);

        StringBuffer sql = new StringBuffer("insert into ");
        sql.append(getTableName(persistentClass));
        sql.append(" (");

        sql.append(StringUtils.join(columns.iterator(), ", "));
        sql.append(") values (");
        sql.append(StringUtils.repeat("?, ", columns.size() - 1));
        sql.append("?)");

        return sql.toString();
    }

    public String generateSelectAllSQL(Class persistentClass) {
        List<String> columns = getColumns(persistentClass, true);

        StringBuffer sql = new StringBuffer("select ");

        sql.append(StringUtils.join(columns.iterator(), ", "));
        sql.append(" from ");
        sql.append(getTableName(persistentClass));

        return sql.toString();
    }

    private String getTableName(Class persistentClass) {
        String[] parts = StringUtils.split(persistentClass.getName(), ".$");
        return parts[parts.length - 1];
    }

    public List<String> getColumns(Class persistentClass, boolean includeIdColumn) {
        Field[] fields = persistentClass.getDeclaredFields();
        ArrayList<String> result = new ArrayList<String>();
        for (Field field : fields) {
            String name = field.getName();
            if (!(!includeIdColumn && "id".equals(name))) {
                result.add(name);
            }
        }
        return result;
    }
}
