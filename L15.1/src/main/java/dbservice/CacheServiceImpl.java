package dbservice;

import base.DataSet;
import cache.CacheEngine;
import cache.CacheEngineImpl;
import cache.MyElement;
import front.FrontendServiceImpl;
import messageSystem.Address;
import messageSystem.MessageSystem;
import messageSystem.MessageSystemContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

//@Service
@Component
public class CacheServiceImpl implements DBService {
    private static final Logger logger = Logger.getLogger(FrontendServiceImpl.class.getName());
    private Long counter=0L;

    public CacheServiceImpl(MessageSystemContext context, Address address, CacheEngine cacheEngine){
        this.context=context;
        this.address=address;
        this.cacheEngine=cacheEngine;
    }



    public void setAddress(Address address) {
        this.address = address;
    }

    //@Autowired
    @Qualifier("dbAddress")
    private Address address;

    public MessageSystemContext getContext() {
        return context;
    }

    public void setContext(MessageSystemContext context) {
        this.context = context;
    }

    //@Autowired
    private MessageSystemContext context;


/*
    public CacheEngineImpl getCacheEngine() {
        return cacheEngine;
    }

    public void setCacheEngine(CacheEngineImpl cacheEngine) {
        this.cacheEngine = cacheEngine;
    }*/

    //@Autowired
    //private CacheEngineImpl cacheEngine;

    //@Autowired
    private CacheEngine cacheEngine;

    @Override
    public <T extends DataSet> void save(T user) {
        user.setId(counter);
        cacheEngine.put(counter,new MyElement(user));
        counter++;
    }

    @Override
    public <T extends DataSet> T load(long id, Class<T> clazz) {
        if(cacheEngine.get(id)!=null) {
            return (T) cacheEngine.get(id).getValue();
        }
        else{
            return null;
        }
    }

    @Override
    public long count() {
        logger.info("Считаем кол-во элементов");
        return cacheEngine.getSize();
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
