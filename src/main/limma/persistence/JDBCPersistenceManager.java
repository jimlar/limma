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

        return runWithConnection(new ConnectionBlock() {
            public Object run(Connection connection) {
                runWithStatement(sqlGenerator.generateInsertSQL(o), connection, new StatementBlock() {
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

                return runWithStatement(sqlGenerator.generateLastIdentitySQL(), connection, new StatementBlock() {
                    public Object run(PreparedStatement statement) throws SQLException {
                        ResultSet result = statement.executeQuery();
                        if (result.next()) {
                            Object id = result.getObject(1);
                            setField(o, persistentClass, "id", id);
                            return o;
                        }
                        throw new RuntimeException("Could not populate object with generated id, no id found");
                    }
                });
            }
        });
    }

    public List loadAll(final Class persistentClass) {
        String sql = sqlGenerator.generateSelectAllSQL(persistentClass);
        return (List) runWithStatement(sql, new StatementBlock() {
            public Object run(PreparedStatement statement) throws SQLException {
                statement.execute();
                ResultSet resultSet = statement.getResultSet();
                return unmarshalObjects(resultSet, persistentClass);
            }
        });
    }


    public void save(Object o) {

    }

    public void delete(Object o) {
    }


    private Object runWithStatement(final String sql, final StatementBlock statementBlock) {
        return runWithConnection(new ConnectionBlock() {
            public Object run(Connection connection) {
                return runWithStatement(sql, connection, statementBlock);
            }
        });
    }

    private Object runWithStatement(String sql, Connection connection, final StatementBlock statementBlock) {
        System.out.println("sql = " + sql);

        PreparedStatement statement = null;
        try {
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
        }
    }

    private Object runWithConnection(ConnectionBlock connectionBlock) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(config.getUrl(), config.getUsername(), config.getPassword());
            return connectionBlock.run(connection);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    private List unmarshalObjects(ResultSet resultSet, Class persistentClass) {
        try {
            List<String> columns = sqlGenerator.getColumns(persistentClass, true);
            ArrayList result = new ArrayList();
            while (resultSet.next()) {
                Object o = persistentClass.newInstance();
                for (String columnName : columns) {
                    setField(o, persistentClass, columnName, resultSet.getObject(columnName));
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
        }
    }

    private void setField(Object o, Class persistentClass, String fieldName, Object value) {
        try {
            Field field = persistentClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static interface StatementBlock {
        Object run(PreparedStatement statement) throws SQLException;
    }

    private static interface ConnectionBlock {
        Object run(Connection connection);
    }
}
