package connection;

import base.DBService;
import base.DataSet;
import executor.LogExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBServiceImpl extends ConnectionHelper implements DBService {
    private static final String CREATE_TABLE_USER = "create table if not exists %s (id bigint NOT NULL auto_increment primary key, name varchar(255),age int(3))";
    private static final String INSERT_DATA_SET = "insert into %s (%s) VALUES ('%s')";
    private static final String SElECT = "select %s from %s where id = '%d'";
    private static final String DELETE_USER = "drop table %s";

    public static final String SEMICOLON_DELIMETER = ",";
    public static final String SEMICOLON_DELIMETER_WITH_QUOTES = "\',\'";
    public static final String USER_TABLE_NAME = "user";
    private final Connection connection;

    public DBServiceImpl() {
        connection = ConnectionHelper.getConnection();
    }

    @Override
    public void prepareTables() throws SQLException {
        LogExecutor exec = new LogExecutor(getConnection());
        String query = String.format(CREATE_TABLE_USER, USER_TABLE_NAME);
        exec.execUpdate(query);
        System.out.println("Table created");
    }


    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        Field[] fields = user.getClass().getDeclaredFields();
        StringJoiner joinerKeys = new StringJoiner(SEMICOLON_DELIMETER);
        StringJoiner joinerValues = new StringJoiner(SEMICOLON_DELIMETER_WITH_QUOTES);
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            joinerKeys.add(fields[i].getName());
            try {
                joinerValues.add(fields[i].get(user).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String query = String.format(INSERT_DATA_SET, USER_TABLE_NAME, joinerKeys.toString(), joinerValues.toString());
        System.out.println("Query:" + query);
        LogExecutor exec = new LogExecutor(getConnection());
        int rows = exec.execUpdate(query);
        System.out.println("User added. Rows changed: " + rows);

    }


    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        LogExecutor exec = new LogExecutor(getConnection());
        StringJoiner joinerKeys = new StringJoiner(SEMICOLON_DELIMETER);
        getFieldNames(clazz).forEach((item) -> {
            joinerKeys.add(item);
        });
        String query = String.format(SElECT, joinerKeys.toString(), USER_TABLE_NAME, id);
        System.out.println("Query:" + query);

        return exec.execQuery(query, resultSet -> {
            DataSet dataSet = null;
            try {
                resultSet.first();
                dataSet = setFields(clazz, resultSet);
            } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return (T) dataSet;
        });
    }

    private <T> T setFields(Class<T> clazz, ResultSet resultSet) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException, NoSuchFieldException {
        Constructor<T> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        T result = constructor.newInstance();

        Map<String, Field> fields = getFields(clazz);
        fields.forEach((k, v) -> {
            v.setAccessible(true);
            try {
                v.set(result, resultSet.getObject(k));
            } catch (IllegalAccessException | SQLException e) {
                e.printStackTrace();
            }
        });
        return result;
    }

    private Map<String, Field> getFields(Class<?> clazz) {
        Map<String, Field> fields = new HashMap<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fields.containsKey(field.getName())) {
                    fields.put(field.getName(), field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    private List<String> getFieldNames(Class<?> clazz) {
        List<String> fields = new ArrayList<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fields.contains(field.getName())) {
                    fields.add(field.getName());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }


    @Override
    public void deleteTables() throws SQLException {
        LogExecutor exec = new LogExecutor(getConnection());
        String query = String.format(DELETE_USER, USER_TABLE_NAME);
        exec.execUpdate(query);
        System.out.println("Table dropped");
    }


    @Override
    public void close() throws Exception {
        connection.close();
        System.out.println("Connection closed. Bye!");
    }


    @Override
    public String getMetaData() {
        try {
            return "Connected to: " + getConnection().getMetaData().getURL() + "\n" +
                    "DB name: " + getConnection().getMetaData().getDatabaseProductName() + "\n" +
                    "DB version: " + getConnection().getMetaData().getDatabaseProductVersion() + "\n" +
                    "Driver: " + getConnection().getMetaData().getDriverName();
        } catch (SQLException e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }
}
