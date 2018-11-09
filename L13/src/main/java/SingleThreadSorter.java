import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class SingleThreadSorter implements Callable {
    private static Logger logger = LoggerFactory.getLogger(SingleThreadSorter.class);
    private int[] arr;

    private static int[] sort(int[] array) {
        Arrays.sort(array);
        return array;
    }

    public SingleThreadSorter(int[] val) {
        arr = val;
    }

    public Object call() {
        sort(arr);
        logger.info(Arrays.toString(arr));
        return arr;
    }
}
