package base;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public interface DBService extends AutoCloseable {
    String getMetaData();

    void prepareTables() throws SQLException;

    void deleteTables() throws SQLException;

    <T extends DataSet> void save(T user) throws SQLException;

    <T extends DataSet> T load(long id, Class<T> clazz) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, SQLException;
}
