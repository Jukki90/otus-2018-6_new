package cache;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.*;

/**
 * -Xmx128m -Xms128m -Dlog4j.configuration=logback.xml
 */

public class CacheTest {
    private static Logger logger = LoggerFactory.getLogger(CacheTest.class);
    private List<String> loadMemoryList = new ArrayList<>();
    private int size;

    @Test
    public void weaknessTest() {
        int size = 20000;
        int miss;
        CacheEngine<Integer, BigObject> cache = new CacheEngineImpl<>(size, 120000, 0);
        for (int i = 0; i < size; i++) {
            cache.put(i, new MyElement<>(new BigObject()));
            logger.info("String for {} : {}", i, (cache.get(i) != null ? cache.get(i).getValue() : "null"));
        }
        loadMemory();
        System.gc();
        logger.info("After GC ----------");
        int nullObjects = 0;
        for (int i = 0; i < size; i++) {
            MyElement<BigObject> item = cache.get(i);
            if (item != null) {
                logger.info("String for {} : {}", i, item.getValue());

            } else {
                nullObjects++;
                logger.info("String for {} : {}", i, null);
            }
        }
        logger.info("Missed Objects: {}", nullObjects);
        cache.dispose();
        assertTrue("Все объекты кэша остались на месте", nullObjects > 0);
    }


    @Test
    public void lifeTimeTest() {
        size = 10;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 1000, 0);
        for (int i = 0; i < size; i++) {
            cache.put(i, new MyElement<>("String: " + i));
        }

        logger.info("Cache hits: {}", cache.getHitCount());
        logger.info("Cache misses: {}", cache.getMissCount());

        await().atMost(4, SECONDS).until(() -> (cache.getMissCount() == size));

        logger.info("Result cache:");
        for (int i = 0; i < size; i++) {
            MyElement<String> element = cache.get(i);
            logger.info("String for {} : {}", i, (element != null ? element.getValue() : "null"));
        }
        logger.info("Cache hits: {}", cache.getHitCount());
        logger.info("Cache misses: {}", cache.getMissCount());
        cache.dispose();
    }

    @Test
    public void maxElementsTest() {
        size = 5;
        int offset = 2;
        CacheEngine<Integer, String> cache = new CacheEngineImpl<>(size, 100000, 0);
        for (int i = 0; i < size + offset; i++) {
            cache.put(i, new MyElement<>(String.valueOf(i)));
        }
        for (int i = 0; i < offset; i++) {
            assertNull("Лишние элементы не затерлись! ", cache.get(i));
        }
        for (int i = 2; i < size + offset; i++) {
            assertEquals("Значение элементов кэша отлично от ожидаемого!", String.valueOf(i), cache.get(i).getValue());
        }
        cache.dispose();
    }


    static class BigObject {
        final byte[] array = new byte[1024 * 1024];

        public byte[] getArray() {
            return array;
        }
    }

    private void loadMemory() {
        for (int i = 0; i < 1200000; i++) {
            loadMemoryList.add(i + "");
        }
    }
}
