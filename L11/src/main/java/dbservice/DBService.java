package dbservice;

import base.DataSet;

public interface DBService extends AutoCloseable {

    <T extends DataSet> void save(T user);

    <T extends DataSet> T load(long id, Class<T> clazz);
}
