package limma.persistence;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class JDBCPersistenceManager implements PersistenceManager {
    private PersistenceConfig config;
    private SQLGenerator sqlGenerator;

    public JDBCPersistenceManager(PersistenceConfig config, SQLGenerator sqlGenerator) {
        this.config = config;
        this.sqlGenerator = sqlGenerator;
        try {
            Class.forName(config.getDriver());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addPersistentClass(Class clazz) {
    }

    public Object create(final Object o) {
        final Class persistentClass = o.getClass();

        return executeSql(sqlGenerator.generateInsertSQL(o), new StatementBlock() {
            public Object run(PreparedStatement statement) throws SQLException {
                for (ListIterator<String> i = sqlGenerator.getColumns(persistentClass, false).listIterator(); i.hasNext();)
                {
                    String column = i.next();
                    try {
                        Field field = persistentClass.getDeclaredField(column);
                        field.setAccessible(true);
                        statement.setObject(i.previousIndex() + 1, field.get(o));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchFieldException e) {
                        throw new RuntimeException(e);
                    }
                }
                statement.execute();
                return o;
            }
        });


        /*
        För att hämta ID't

        mysql: select last_insert_id()
        hsql:  call identity()
         */
    }

    public List loadAll(final Class persistentClass) {
        String sql = sqlGenerator.generateSelectAllSQL(persistentClass);
        return (List) executeSql(sql, new StatementBlock() {
            public Object run(PreparedStatement statement) throws SQLException {
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                return unmarschalObjects(resultSet, persistentClass);
            }
        });
    }


    public void save(Object o) {

    }

    public void delete(Object o) {
    }


    private Object executeSql(String sql, StatementBlock statementBlock) {
        System.out.println("sql = " + sql);
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            statement = connection.prepareStatement(sql);
            return statementBlock.run(statement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private List unmarschalObjects(ResultSet resultSet, Class persistentClass) {
        try {
            ArrayList result = new ArrayList();
            while (resultSet.next()) {
                Object o = persistentClass.newInstance();
                List<String> columns = sqlGenerator.getColumns(persistentClass, true);
                for (String column : columns) {
                    Field field = persistentClass.getDeclaredField(column);
                    field.setAccessible(true);
                    field.set(o, resultSet.getObject(column));
                }
                result.add(o);
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static interface StatementBlock {
        Object run(PreparedStatement statement) throws SQLException;
    }
}
