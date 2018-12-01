package dbservice;

import base.DataSet;
import cache.CacheEngine;
import cache.MyElement;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.MessageSystemContext;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service
public class CacheServiceImpl implements DBService {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);
    private Long counter = 0L;

    public CacheServiceImpl(MessageSystemContext context, Address address, CacheEngine cacheEngine) {
        this.context = context;
        this.address = address;
        this.cacheEngine = cacheEngine;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Qualifier("dbAddress")
    private Address address;

    public MessageSystemContext getContext() {
        return context;
    }

    public void setContext(MessageSystemContext context) {
        this.context = context;
    }

    private MessageSystemContext context;

    public CacheEngine getCacheEngine() {
        return cacheEngine;
    }

    public void setCacheEngine(CacheEngine cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    private CacheEngine cacheEngine;

    @Override
    public <T extends DataSet> void save(T user) {
        user.setId(counter);
        cacheEngine.put(counter, new MyElement(user));
        counter++;
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        if (cacheEngine.get(id) != null) {
            return (T) cacheEngine.get(id).getValue();
        } else {
            return null;
        }
    }

    @Override
    public long count() {

        long size = cacheEngine.getSize();
        logger.info("Считаем кол-во элементов {}",size);
        return size;
    }

    @Override
    public void init() {
        logger.info("Start init method for db service");
        context.getMessageSystem().addAddressee(this);
    }

    @Override
    public void close() throws Exception {
        cacheEngine.dispose();
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public MessageSystem getMS() {
        return context.getMessageSystem();
    }
}
