package dao;

import base.DataSet;
import org.hibernate.Session;

public abstract class DataSetDAO {
    protected Session session;

    public DataSetDAO(Session session) {
        this.session = session;
    }

    abstract void save(DataSet data);

    abstract DataSet read(long id);
}
