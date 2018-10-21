package dbservice;

import base.AddressDataSet;
import base.DataSet;
import base.PhoneDataSet;
import base.UserDataSet;
import dao.UserDataSetDAOImpl;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.function.Function;

public class DBServiceHibernateImpl implements DBService {
    private final SessionFactory sessionFactory;

    public DBServiceHibernateImpl() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(UserDataSet.class);
        configuration.addAnnotatedClass(PhoneDataSet.class);
        configuration.addAnnotatedClass(AddressDataSet.class);
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/hw10?serverTimezone=Europe/Moscow&useLegacyDatetimeCode=false");
        configuration.setProperty("hibernate.connection.username", "user");
        configuration.setProperty("hibernate.connection.password", "qwerty");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create");
        configuration.setProperty("hibernate.connection.useSSL", "false");
        configuration.setProperty("hibernate.enable_lazy_load_no_trans", "true");
        configuration.setProperty("hibernate.jdbc.time_zone", "UTC");
        sessionFactory = createSessionFactory(configuration);
    }

    public DBServiceHibernateImpl(Configuration configuration) {
        sessionFactory = createSessionFactory(configuration);
    }

    private static SessionFactory createSessionFactory(Configuration configuration) {
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        builder.applySettings(configuration.getProperties());
        ServiceRegistry serviceRegistry = builder.build();
        return configuration.buildSessionFactory(serviceRegistry);
    }


    public void save(UserDataSet dataSet) {
        try (Session session = sessionFactory.openSession()) {
            UserDataSetDAOImpl dao = new UserDataSetDAOImpl(session);
            dao.save(dataSet);
            session.save(dataSet);

        }
    }


    public void close() {
        sessionFactory.close();
    }

    private <R> R runInSession(Function<Session, R> function) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            R result = function.apply(session);
            transaction.commit();
            return result;
        }
    }


    @Override
    public <T extends DataSet> void save(T user) {
        try (Session session = sessionFactory.openSession()) {
            UserDataSetDAOImpl dao = new UserDataSetDAOImpl(session);
            dao.save((UserDataSet) user);
            session.save(user);
        }
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {

        return runInSession(session -> {
            UserDataSetDAOImpl dao = new UserDataSetDAOImpl(session);
            UserDataSet object = dao.read(id);
            Hibernate.initialize(object);
            return (T) object;
        });
    }

    @Override
    public long count() {
        return runInSession(session -> {
            UserDataSetDAOImpl dao = new UserDataSetDAOImpl(session);
            return dao.count();
        });
    }

}
