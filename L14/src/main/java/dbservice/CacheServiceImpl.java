package dbservice;

import base.DataSet;
import cache.CacheEngineImpl;
import cache.MyElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheServiceImpl implements DBService {
    private Long counter=0L;

    public CacheEngineImpl getCacheEngine() {
        return cacheEngine;
    }

    public void setCacheEngine(CacheEngineImpl cacheEngine) {
        this.cacheEngine = cacheEngine;
    }

    @Autowired
    private CacheEngineImpl cacheEngine;

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
        return cacheEngine.getSize();
    }

    @Override
    public void close() throws Exception {
        cacheEngine.dispose();
    }
}
