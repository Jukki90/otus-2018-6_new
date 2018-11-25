package dao;

import base.DataSet;
import base.UserDataSet;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class UserDataSetDAOImpl extends DataSetDAO {

    public static final String SELECT_COUNT_FROM = "select count(*) from UserDataSet u";
    public static final String USERS_TABLE = "users";

    public UserDataSetDAOImpl(Session session) {
        super(session);
    }

    public void save(UserDataSet dataSet) {
        session.save(dataSet);
    }

    @Override
    public void save(DataSet data) {
        session.save(data);
    }

    public UserDataSet read(long id) {
        return session.load(UserDataSet.class, id);
    }

    @Override
    public long count() {
        Query query = session.createQuery(
                SELECT_COUNT_FROM);
        return (long) query.uniqueResult();
    }

    public UserDataSet readByName(String name) {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        Root<UserDataSet> from = criteria.from(UserDataSet.class);
        criteria.where(builder.equal(from.get("name"), name));
        Query<UserDataSet> query = session.createQuery(criteria);
        return query.uniqueResult();
    }

    public List<UserDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<UserDataSet> criteria = builder.createQuery(UserDataSet.class);
        criteria.from(UserDataSet.class);
        return session.createQuery(criteria).list();
    }

}
