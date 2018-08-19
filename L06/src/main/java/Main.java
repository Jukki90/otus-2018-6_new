import cache.CacheEngine;
import cache.CacheEngineImpl;
import cache.MyElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        int size = 5;
        int cycleNums = 30;

        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 5000, 0);
        for (int i = 0; i < size; i++) {
            cache.put(new MyElement<>(i, "String: " + i));
        }

        for (int j = 0; j < cycleNums; j++) {
            logger.info("Cycle number: {}", j);

            for (int i = 0; i < size; i++) {
                MyElement<Integer, String> element = cache.get(i);
                logger.info("String for {} : {}", i, (element != null ? element.getValue() : "null"));
            }
            logger.info("Cache hits: {}", cache.getHitCount());
            logger.info("Cache misses: {}", cache.getMissCount());

            Thread.sleep(1000);
        }
        cache.dispose();
    }
}
