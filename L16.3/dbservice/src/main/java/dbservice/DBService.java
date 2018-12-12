package dbservice;

import base.DataSet;
import messageSystem.Addressee;

public interface DBService extends AutoCloseable, Addressee {

    <T extends DataSet> void save(T user);

    <T extends DataSet> T load(long id, Class<T> clazz);

    long count();

    void init();
}
