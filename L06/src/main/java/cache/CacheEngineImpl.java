package cache;

import java.lang.ref.SoftReference;
import java.util.*;
import java.util.function.Function;

public class CacheEngineImpl<K, V> implements CacheEngine<K, V> {
    private static final int TIME_THRESHOLD_MS = 5;

    private final int maxElements;
    private final long lifeTimeMs;
    private final long idleTimeMs;

    private int hit = 0;
    private int miss = 0;

    private Map<K, SoftReference<MyElement<V>>> elements = new LinkedHashMap<>();
    private final Timer timer = new Timer();

    public CacheEngineImpl(int maxElements, long lifeTimeMs, long idleTimeMs) {
        this.maxElements = maxElements;
        this.lifeTimeMs = lifeTimeMs > 0 ? lifeTimeMs : 0;
        this.idleTimeMs = idleTimeMs > 0 ? idleTimeMs : 0;
    }

    @Override
    public synchronized void put(K key, MyElement<V> element) {
        if (elements.size() == maxElements) {
            K firstKey = elements.keySet().iterator().next();
            elements.remove(firstKey);
        }

        elements.put(key, new SoftReference<>(element));

        if (lifeTimeMs != 0) {
            TimerTask lifeTimerTask = getTimerTask(key, lifeElement -> lifeElement.getCreationTime() + lifeTimeMs);
            timer.schedule(lifeTimerTask, lifeTimeMs);
        }
        if (idleTimeMs != 0) {
            TimerTask idleTimerTask = getTimerTask(key, idleElement -> idleElement.getLastAccessTime() + idleTimeMs);
            timer.schedule(idleTimerTask, idleTimeMs, idleTimeMs);
        }
    }

    @Override
    public MyElement<V> get(Object key) {

        SoftReference<MyElement<V>> item = elements.get(key);
        MyElement<V> element = (Objects.isNull(item) ? null : item.get());
        if (element != null) {
            hit++;
            element.setAccessed();
        }
        return element;
    }

    @Override
    public int getHitCount() {
        return hit;
    }

    @Override
    public int getMissCount() {
        return miss;
    }

    @Override
    public void dispose() {
        timer.cancel();
    }

    private TimerTask getTimerTask(final K key, Function<MyElement<V>, Long> timeFunction) {
        return new TimerTask() {
            @Override
            public void run() {
                if (elements.get(key) != null) {
                    MyElement<V> element = elements.get(key).get();
                    if (element == null || isT1BeforeT2(timeFunction.apply(element), System.currentTimeMillis())) {
                        elements.remove(key);
                        miss++;
                        this.cancel();
                    }
                }
            }
        };
    }

    private boolean isT1BeforeT2(long t1, long t2) {
        return t1 < t2 + TIME_THRESHOLD_MS;
    }

}
