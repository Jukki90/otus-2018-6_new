package connection;

import base.DBService;
import base.DataSet;
import executor.LogExecutor;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.StringJoiner;

public class DBServiceImpl extends ConnectionHelper implements DBService {
    private static final String CREATE_TABLE_USER = "create table if not exists user (id bigint NOT NULL auto_increment primary key, name varchar(255),age int(3))";
    private static final String INSERT_USER = "insert into user (name,age) values ('%s')";
    private static final String INSERT_DATA_SET = "insert into user (%s) VALUES ('%s')";
    private static final String SElECT = "select %s from user where id = '%d'";
    private static final String DELETE_USER = "drop table user";
    private final Connection connection;

    public DBServiceImpl() {
        connection = ConnectionHelper.getConnection();
    }

    @Override
    public void prepareTables() throws SQLException {
        LogExecutor exec = new LogExecutor(getConnection());
        exec.execUpdate(CREATE_TABLE_USER);
        System.out.println("Table created");
    }


    @Override
    public <T extends DataSet> void save(T user) throws SQLException {
        Field[] fields = user.getClass().getDeclaredFields();
        StringJoiner joinerKeys = new StringJoiner(",");
        StringJoiner joinerValues = new StringJoiner("\',\'");
        for (int i = 0; i < fields.length; i++) {
            fields[i].setAccessible(true);
            joinerKeys.add(fields[i].getName());
            try {
                joinerValues.add(fields[i].get(user).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        String query = String.format(INSERT_DATA_SET, joinerKeys.toString(), joinerValues.toString());
        System.out.println("Query:" + query);
        LogExecutor exec = new LogExecutor(getConnection());
        int rows = exec.execUpdate(query);
        System.out.println("User added. Rows changed: " + rows);

    }


    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) throws SQLException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        LogExecutor exec = new LogExecutor(getConnection());
        String query = String.format(SElECT, "id,name,age", id);
        System.out.println("Query:" + query);
        return exec.execQuery(query, resultSet -> {
            DataSet dataSet = null;
            try {
                resultSet.first();
                Field[] fields = (Field[]) ArrayUtils.addAll(DataSet.class.getDeclaredFields(), clazz.getDeclaredFields());

                Object[] constructorArguments = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    constructorArguments[i] = resultSet.getObject(fields[i].getName());
                }
                Class<?>[] classes = toClasses(constructorArguments);
                dataSet = clazz.getDeclaredConstructor(classes).newInstance(constructorArguments);
            } catch (SQLException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return (T) dataSet;
        });
    }


    @Override
    public void deleteTables() throws SQLException {
        LogExecutor exec = new LogExecutor(getConnection());
        exec.execUpdate(DELETE_USER);
        System.out.println("Table dropped");
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
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
